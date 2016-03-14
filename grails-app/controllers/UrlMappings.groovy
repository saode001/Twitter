class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

       "/accounts"(resources:'account') {
          "/messages"(resources: "message" )}


       // "/api/accounts"(resources: "account")
       // "/api/messages"(resources: "message")


        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
