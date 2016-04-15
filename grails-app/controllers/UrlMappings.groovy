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

		"/"(view: 'index')
        "500"(controller: 'Error', action: 'internalServerError')
        "404"(controller: 'Error', action: 'notFound')
        "401"(controller: 'Error', action: 'unauthorized')
        "403"(controller: 'Error', action: 'forbidden')
    }
}