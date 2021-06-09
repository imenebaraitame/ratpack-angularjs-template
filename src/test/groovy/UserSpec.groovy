import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.MainClassApplicationUnderTest
import spock.lang.*
import ratpack.http.client.ReceivedResponse
import ratpack.http.client.RequestSpec
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import ratpack.func.Action as RatpackAction
import ratpack.http.Response
import ratpack.test.http.TestHttpClient
import ratpack.http.Status

class UserSpec extends Specification {


  @AutoCleanup
  @Shared
  GroovyRatpackMainApplicationUnderTest groovyScriptApplicationUnderTest = new GroovyRatpackMainApplicationUnderTest()

  @Shared
  TestHttpClient testClient = groovyScriptApplicationUnderTest.httpClient

  @Unroll
  def 'Home page should return ok'(){
    when:
      def response = aut.httpClient.get()
    then:
      response.statusCode == 200//OK
    where:
      aut                              | type
      groovyScriptApplicationUnderTest | 'Ratpack.groovy'
  }

  @Unroll
  def 'Should not create a user'(){
    def admin = new JsonBuilder(new User(id:1, username:"admin",password:"admin",isActive:true)).toString()
    when:
      aut.httpClient.requestSpec({ RequestSpec spec ->
          spec.body.type('application/json')
          spec.body.text(admin)
      } as RatpackAction)
      def response = aut.httpClient.post('users')
    then:
      response.statusCode == 401 ////Unauthorized
    and:
      response.body.text != admin
    where:
      aut                              | type
      groovyScriptApplicationUnderTest | 'Ratpack.groovy'
  }

  @Unroll
  def 'Should not access to users list'() {
      when:
      testClient.requestSpec({ RequestSpec requestSpec ->
          requestSpec.body.type('application/json')
          requestSpec.headers.set("AUTHORIZATION", "Bearer 74ca99d3-8f2d-4925-90d0-ea16c87ea41c")
      } as RatpackAction)
      ReceivedResponse response = testClient.get("users")
      then:
        response.status == Status.of(401)//Unauthorized
        response.body.text != ''
  }

  @Unroll
  def 'Should register a user then perform CRUD operations'() {

      def jsonParser = new JsonSlurper()
      User admin = new User(id:1, username:"admin",password:"admin",isActive:true)

      when: 'register a admin user'
      def response1 = testClient.requestSpec({ def spec ->
          spec.body.type('application/json')
          spec.body.text(new JsonBuilder(admin).toString())
      } as RatpackAction).post("api/register")
      then: 'should return true result'
        response1.status == Status.of(200)//OK
        jsonParser.parseText(response1.body.text).result == true

      when: 'login with admin user'
      def response2 = testClient.requestSpec({ def spec ->
          spec.body.type('application/json')
          spec.body.text(new JsonBuilder(["username":admin.username,"password":admin.password]).toString())
      } as RatpackAction).post("api/login")
      def token = jsonParser.parseText(response2.body.text).token
      then: 'should get a valid token'
        response2.status == Status.of(200)//OK
        token.size() == 432

      when: 'trying to get list of users'
      def response3 = testClient.requestSpec({ def spec ->
          spec.body.type('application/json')
          spec.headers.set("AUTHORIZATION", "Bearer ${token}")
      } as RatpackAction).get("users")
      then: 'should retreive admin user in a list'
        response3.status == Status.of(200)//OK
        (jsonParser.parseText(response3.body.text).toList()[0] as User) == admin

      when: 'trying to delete admin user'
      def response4 = testClient.requestSpec({ def spec ->
          spec.body.type('application/json')
          spec.headers.set("AUTHORIZATION", "Bearer ${token}")
      } as RatpackAction).delete("users/${admin.id}")
      then: 'should return user id'
        response4.status == Status.of(200)//OK
        jsonParser.parseText(response4.body.text).id == admin.id

      when: 'trying to get list of users'
      def response5 = testClient.requestSpec({ def spec ->
          spec.body.type('application/json')
          spec.headers.set("AUTHORIZATION", "Bearer ${token}")
      } as RatpackAction).get("users")
      then: 'should return an empty list'
        response5.status == Status.of(200)//OK
        jsonParser.parseText(response5.body.text).toList() == []

      when: 'logout admin user'
      def response6 = testClient.requestSpec({ def spec ->
          spec.body.type('application/json')
          spec.headers.set("AUTHORIZATION", "Bearer ${token}")
      } as RatpackAction).post("api/logout")
      then: 'should return ok status and redirect to home page'
        response6.status == Status.of(200)//OK
        response6.body.text != '' // it returns HTML page

  }

}
