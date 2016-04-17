class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }
        "/"(view: '/index')

        "/api/accounts"(resources:'account') {
            "/messages"(resources:'message')
        }

        "/api/accounts/$id/follow/$followId"(controller:'account',action:'followAccount')

        "/api/accounts/$id/followers"(controller:'account',action:'showFollowers')

        "/api/messages/search/$searchTerm"(controller: 'message',action:'searchMessages')

        "/api/accounts/$id/feed"(controller: 'account',action:'returnFeed')


        "500"(controller: 'Error', action: 'internalServerError')
        "404"(controller: 'Error', action: 'notFound')
        "401"(controller: 'Error', action: 'unauthorized')
        "403"(controller: 'Error', action: 'forbidden')
    }
}