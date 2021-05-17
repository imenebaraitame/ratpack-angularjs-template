// import ratpack.groovy.template.MarkupTemplateModule
// import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.jackson.Jackson.json
import static ratpack.jackson.Jackson.fromJson
import static ratpack.groovy.Groovy.ratpack
import ratpack.hikari.HikariModule
import ratpack.service.Service
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

ratpack {
  serverConfig {
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
    bind H2ConnectionDataSource
    bind UserService

    add Service.startup('startup'){ def event ->
        // Create "users" table on startup
        ConnectionSource connectionSource = event.registry.get(H2ConnectionDataSource).connectionSource
        TableUtils.createTableIfNotExists(connectionSource, User)
    }

  } // bindings

  handlers {
    // get {
    //   render groovyMarkupTemplate("index.gtpl", title: "My Ratpack App")
    // }

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
