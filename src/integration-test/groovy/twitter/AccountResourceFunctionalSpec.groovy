package twitter

/**
 * Created by Priyanka on 4/12/2016.
  */

import geb.spock.GebSpec
import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import spock.lang.*

@Integration
@Stepwise
@Ignore
class AccountResourceFunctionalSpec  extends GebSpec{
    RESTClient restClient

    @Shared
    def token

    def setup() {
        restClient = new RESTClient(baseUrl)
    }

    def 'TL3: Invalid login will be without token and will be rejected'() {
        when:
        restClient.get(path: '/api/accounts')

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 403
        problem.message.contains('Forbidden')
    }

    def 'passing a valid username and passowrd generates a token'() {
        setup:
        def authentication = ([username: 'admin', password: 'Security1testing'] as JSON) as String

        when:
        def response = restClient.post(path: '/api/login', body: authentication, requestContentType: 'application/json')

        then:
        response.status == 200
        response.data.username == 'admin'
        response.data.roles == ['ROLE_READ']
        //noinspection GroovyDoubleNegation
        !!(token = response.data.access_token)
    }

    def 'using token access to restaurants endpoint allowed'() {
        when:
        def response = restClient.get(path: '/api/accounts', headers: ['X-Auth-Token': token])

        then:
        response.status == 200
        response.data.size() == 0
    }

}
