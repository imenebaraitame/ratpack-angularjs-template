// import ratpack.groovy.template.MarkupTemplateModule
// import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.jackson.Jackson.json
import static ratpack.groovy.Groovy.ratpack

ratpack {
  serverConfig {
    port(8080) // default port = 5050
  }
  // bindings {
  //   module MarkupTemplateModule
  // }

  handlers {
    // get {
    //   render groovyMarkupTemplate("index.gtpl", title: "My Ratpack App")
    // }

    // path('image') {
    //   byMethod {
    //     get {
    //       render json([:])
    //     }
    //     post {
    //       render json([:])
    //     }
    //   }
    // }


    files { dir "public" indexFiles 'index.html' }


  } // handlers

}
