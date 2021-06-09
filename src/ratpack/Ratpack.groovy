// import ratpack.groovy.template.MarkupTemplateModule
// import static ratpack.groovy.Groovy.groovyMarkupTemplate
// import static ratpack.groovy.Groovy.groovyTemplate
// import ratpack.session.Session
import com.fasterxml.jackson.databind.JsonNode
import com.zaxxer.hikari.HikariConfig
import ratpack.http.Status
import ratpack.service.StartEvent
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
import org.pac4j.http.client.direct.HeaderClient
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

String JWT_SALT = '12345678901234567890123456789012' // length = 32

ratpack {
  serverConfig {
    development(true)
    port(3000) // default port = 5050
  }
  // bindings {
  //   module MarkupTemplateModule
  // }

  bindings {
    module HikariModule, { HikariConfig config ->
        config.addDataSourceProperty("URL", "jdbc:h2:mem:users;INIT=CREATE SCHEMA IF NOT EXISTS DEV")
        config.dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
    }

    module SessionModule

    bind H2ConnectionDataSource
    bind UserService
    bindInstance(new AuthenticatorService())

    add Service.startup('startup'){ StartEvent event ->
        // Create "users" table on startup
        ConnectionSource connectionSource = event.registry.get(H2ConnectionDataSource).connectionSource
        TableUtils.createTableIfNotExists(connectionSource, User)
    }

  } // bindingsJwtGenerator

  handlers {

    // Direct client authenticator
    final def signatureConfiguration = new SecretSignatureConfiguration(JWT_SALT)
    final def encryptionConfiguration = new SecretEncryptionConfiguration(JWT_SALT)
    final def jwtAuthenticator = new JwtAuthenticator([signatureConfiguration], [encryptionConfiguration])
    final def parameterClient = new ParameterClient("token", jwtAuthenticator)
    final def headerClient = new HeaderClient("Authorization", "Bearer ", jwtAuthenticator)
    parameterClient.supportGetRequest = true
    parameterClient.supportPostRequest = true

    // all(RatpackPac4j.authenticator('callback', parameterClient))
    all(RatpackPac4j.authenticator(parameterClient, headerClient))

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

      path("login") { UserService userService, Context ctx ->
        parse(jsonNode()).then({ JsonNode data ->
                          // TODO: you can check if there are some users in the DB first...
                          userService.findByUsernameAndPassword( data.get('username').asText(),  data.get('password').asText() ).then { List<User> users ->
                            if (users.isEmpty()){
                              ctx.response.status(Status.NOT_FOUND).send('Username or password is incorrect')
                            } else {
                              User user = users.first()
                              if (!user.isActive) {
                                ctx.response.status(Status.FORBIDDEN).send('Sorry! Your account is not activated.')
                              } else {
                                  Blocking.get({
                                    CommonProfile model = ctx.get(AuthenticatorService).authenticate(data, user)
                                    JwtGenerator generator = new JwtGenerator(signatureConfiguration, encryptionConfiguration)
                                    return generator.generate(model)
                                  }).onError({ Throwable e ->
                                    ctx.response.status(Status.BAD_REQUEST).send(e.message)
                                  }).then({ String token ->
                                    render json(['token': token])
                                  })
                                }
                            }
                          }

                        })
          } // path "/api/login"

          post("logout") { Context ctx ->
              RatpackPac4j.logout(ctx).then {
                  redirect("/") // not really needed.
                  // or:
                  // def session = ctx.get(Session)
                  // if (session){
                  //   session.terminate()
                  // }
                  // or:
                  // ctx.next()
              }
          } // post "/api/logout"

          post("register") { Context ctx, UserService userService ->
            parse(fromJson(User)).then { User user ->
              userService.create(user).onError { def e ->
                ctx.response.status(Status.BAD_REQUEST).send(e.message)
              }.then { Integer id ->
                render json([result: (user != null) ])
              }
            }
          } // post "/api/register"

    } // prefix "/api"



    prefix('users') {

      // Prevent access to all next coming handlers
      all(RatpackPac4j.requireAuth( HeaderClient.class ))

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
            userService.delete(pathTokens['id']).then { Integer id ->
                render(json(['id': id]))
            }
          }
        }
      }

      all { UserService userService ->

        byMethod {
          // Get all users
          get { Context ctx ->
             RatpackPac4j.userProfile(ctx).route({ Optional<CommonProfile> profile ->
               profile.isPresent()
             }, { Optional<CommonProfile> profile ->
              userService.all.then { List<User> users ->
                // You can also used `profile.get().attributes` which is a user profile
                render json(users)
              }
             }).then{ Optional<CommonProfile> profile ->
                 ctx.response.status(Status.FORBIDDEN).send("Access denied${profile == Optional.empty()?', you need to login':'. An error has occurred'}.")
             }

          }
          // Create a user
          post { Context ctx ->
            parse(fromJson(User)).then { User user ->
              // It works in case you want to use the default behavior of $resource from angularjs (you can then delete the update PUT method above)
              // if (user.id){
              //   userService.update( user ).then { Integer id ->
              //     render(json(user))
              //   }
              // } else {
                userService.create( user ).onError { Throwable e ->
                  ctx.response.status(Status.BAD_REQUEST).send(e.message)
                }.then { Integer id ->
                  render(json([id: id]))
                }
              // }
            }
          }

        }

      } // path

    } // prefix "/users"

    files { dir "public" indexFiles 'index.html' }

  } // handlers

}
