class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/accounts"(resources:'account') {
            "/messages"(resources:'message')
        }

        "/accounts/$id/follow/$followId"(controller:'account',action:'followAccount')

        "/accounts/$id/followers"(controller:'account',action:'showFollowers')

        "/messages/search/$searchTerm"(controller: 'message',action:'searchMessages')

        "/accounts/$id/feed"(controller: 'account',action:'returnFeed')

        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}