package twitter

import grails.converters.JSON

class ErrorController {

    //def index() {}

    def internalServerError() {
        response.status = 500
        render([error: response.status, message: 'Internal server error'] as JSON)
    }

    def notFound() {
        response.status = 404
        render([error: response.status, message: 'Internal server error'] as JSON)
    }

    def unauthorized() {
        response.status = 401
        render([error: response.status, message: 'Internal server error'] as JSON)
    }

    def forbidden() {
        response.status = 403
        render([error: response.status, message: 'Internal server error'] as JSON)
    }

}