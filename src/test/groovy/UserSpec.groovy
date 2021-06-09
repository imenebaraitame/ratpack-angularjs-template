import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.MainClassApplicationUnderTest
import spock.lang.*
import ratpack.http.client.ReceivedResponse
import ratpack.http.client.RequestSpec
import groovy.json.JsonBuilder
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
  def 'Should not access to users list'() {
      when:
      testClient.requestSpec({ RequestSpec requestSpec ->
          requestSpec.body.type('application/json')
          requestSpec.headers.set("AUTHORIZATION", "Bearer 74ca99d3-8f2d-4925-90d0-ea16c87ea41c")
      } as RatpackAction)
      ReceivedResponse response = testClient.get("users")
      then:
        response.status == Status.of(401)//Unauthorized
        response.body.text != ""
  }

  // @Unroll
  // def 'Should prevent access to listing users'(){
  //   when:
  //     def response = aut.httpClient.get('/users')
  //   then:
  //     response.statusCode == 403
  //   and:
  //     response.body.text == 'Access denied, you need to login.'
  //   where:
  //     aut                              | type
  //     groovyScriptApplicationUnderTest | 'Ratpack.groovy'
  // }

  // @Unroll
  // def 'List of users should be empty'(){
  //   when:
      // aut.httpClient.requestSpec({ RequestSpec requestSpec ->
      //     requestSpec.body.type('application/json')
      //     requestSpec.headers.set("AUTHORIZATION", "Bearer 74ca99d3-8f2d-4925-90d0-ea16c87ea41c")
      //     requestSpec.body.text(new JsonBuilder(["username":"admin","password":"admin","isActive":true]).toString())
      // } as RatpackAction)
  //     def response = aut.httpClient.get('/users')
  //   then:
  //     response.statusCode == 201 //403
  //   and:
  //     response.body.text == '[]'
  //   where:
  //     aut                              | type
  //     groovyScriptApplicationUnderTest | 'Ratpack.groovy'
  // }

  // def 'Should create a user'(){
  //     when:
  //     aut.httpClient.requestSpec({ RequestSpec requestSpec ->
  //         requestSpec.body.type('application/json')
  //         requestSpec.headers.set("AUTHORIZATION", "Bearer 74ca99d3-8f2d-4925-90d0-ea16c87ea41c")
  //         requestSpec.body.text(new JsonBuilder(["username":"admin","password":"admin","isActive":true]).toString())
  //     } as RatpackAction)
  //     ReceivedResponse response = aut.httpClient.post("users")
  //     then:
  //       response.statusCode == 200
  //     where:
  //       aut                              | type
  //       groovyScriptApplicationUnderTest | 'Ratpack.groovy'
  // }

}
