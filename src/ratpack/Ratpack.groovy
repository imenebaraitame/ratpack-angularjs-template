// import ratpack.groovy.template.MarkupTemplateModule
// import static ratpack.groovy.Groovy.groovyMarkupTemplate
// import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.jackson.Jackson.json
import static ratpack.jackson.Jackson.fromJson
import static ratpack.groovy.Groovy.ratpack
import ratpack.handling.Context
import ratpack.hikari.HikariModule
import ratpack.service.Service
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import ratpack.session.SessionModule
import static ratpack.jackson.Jackson.jsonNode
import ratpack.pac4j.RatpackPac4j
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.http.client.direct.ParameterClient
import ratpack.exec.Blocking
import org.pac4j.core.profile.CommonProfile
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration

/**
 * Example auth with cURL:
 * curl -X POST -H 'Content-Type: application/json' -d '{"username":"admin","password":"admin"}' localhost:3000/api/login
 *
 * returns:
 "eyJjdHkiOiJKV1QiLCJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiZGlyIn0..A75iJhXdS4dfiLo9.1cnSo2Z9jR_JLzNk54ikLBEapPdpIfpp_Lo3SQYD96CjdM8UthT8vsjNG2oRhkSqHScRSLoKWrZo0-HkKDj4Jdu0m05imPs8PsuEsz0GjXwG7JQiPe7W135SLMSh4CJWW5QgzmtZg9skriCqq7XpMR16hQe9eonPZfBA45G_tPSmRnoHg0PUMpZgH_e7sMjOnqmnKeuWMRYUPPfcFE0Hp13lE3Vp_00_U_feKBnCtM1l3eBMdKZsiY-j2lBkaAavMRdSBgUlhO4_gyRNA0_JccslpV0BzLnaTDTUFi9GODm3SRrtv3OMPtL6ih4X.nSvhWSKZB9XALzRljXBonQ
 */

def JWT_SALT = '12345678901234567890123456789012' // length = 32

ratpack {
  serverConfig {
    development(true)
    port(3000) // default port = 5050
  }
  // bindings {
  //   module MarkupTemplateModule
  // }

  bindings {
    module HikariModule, { def config ->
        config.addDataSourceProperty("URL", "jdbc:h2:mem:users;INIT=CREATE SCHEMA IF NOT EXISTS DEV")
        config.dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
    }

    module SessionModule

    bind H2ConnectionDataSource
    bind UserService
    bindInstance(new AuthenticatorService())

    add Service.startup('startup'){ def event ->
        // Create "users" table on startup
        ConnectionSource connectionSource = event.registry.get(H2ConnectionDataSource).connectionSource
        TableUtils.createTableIfNotExists(connectionSource, User)
    }

  } // bindings

  handlers {

    final def signatureConfiguration = new SecretSignatureConfiguration(JWT_SALT)
    final def encryptionConfiguration = new SecretEncryptionConfiguration(JWT_SALT)
    final def jwtAuthenticator = new JwtAuthenticator([signatureConfiguration], [encryptionConfiguration])
    final def parameterClient = new ParameterClient("token", jwtAuthenticator)
    parameterClient.supportGetRequest = true
    parameterClient.supportPostRequest = false

    all(RatpackPac4j.authenticator('callback', parameterClient))

    // get {
    //   render groovyMarkupTemplate("index.gtpl", title: "My Ratpack App")
    // }

    // get("login") { Context ctx ->
    //     render groovyTemplate('login.html', [title: "Login",
    //             action: "/$pac4jCallbackPath",
    //             method: 'get',
    //             error: request.queryParams.error ?: ''])
    // }

    // get("logout") { Context ctx ->
    //     RatpackPac4j.logout(ctx).then {
    //         redirect("/")
    //     }
    // }

    prefix("api") {
      path("login") { def ctx ->

        parse(jsonNode()).then({ def data ->
                            Blocking.get({
                                      CommonProfile model = ctx.get(AuthenticatorService).authenticate(data)
                                      JwtGenerator generator = new JwtGenerator(signatureConfiguration, encryptionConfiguration)
                                      return generator.generate(model)
                                  }).onError({ def e ->
                                    ctx.response.status(400)
                                    render e.message
                            }).then({ def token ->
                              render json(['token': token])
                            })
                        })

            post("logout") {
                render json([username:'', password:''])
            }

          }

          post("register") { UserService userService ->
            parse(fromJson(User)).then { User user ->
              userService.create(user).then {
                render json([result: (user != null) ])
              }
            }
          }
    }

    // Prevent access to all next coming handlers
    // all(RatpackPac4j.requireAuth( parameterClient ))

    prefix('users') {

      path(":id") { UserService userService ->
        byMethod {
          // Get a user
          get {
            userService.get(pathTokens['id']).then { User user ->
              render json(user)
            }
          }
          // Update a user
          put {
            parse(fromJson(User)).then { User user ->
              userService.update( user ).then { Integer id ->
                render(json(user))
              }
            }
          }
          // Delete a user
          delete {
            userService.delete(pathTokens['id']).then { def id ->
                render(json(['id': id]))
            }
          }
        }
      }

      all { UserService userService ->

        byMethod {
          // Get all users
          get {
            userService.all.then { List<User> users ->
              render json(users)
            }
          }
          // Create a user
          post {
            parse(fromJson(User)).then { User user ->
              // It works in case you want to use the default behavior of $resource from angularjs (you can then delete the update PUT method above)
              // if (user.id){
              //   userService.update( user ).then { Integer id ->
              //     render(json(user))
              //   }
              // } else {
                userService.create( user ).then { Integer id ->
                  render(json(user))
                }
              // }
            }
          }

        }

      } // path

    } // prefix

    files { dir "public" indexFiles 'index.html' }


  } // handlers

}
