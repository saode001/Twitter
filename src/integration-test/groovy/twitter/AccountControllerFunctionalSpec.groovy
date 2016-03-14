package twitter

import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import spock.lang.*
import geb.spock.*


@Integration
@Stepwise
class AccountControllerFunctionalSpec extends GebSpec {

    @Shared
    def accountId

    RESTClient restClient

    def setup() {
        restClient = new RESTClient(baseUrl)
    }

    def 'get all accounts'(){
        when:
        def resp = restClient.get(path: '/accounts')

        then:
        resp.status == 200
        resp.data.size() == 0
    }

    def 'saves a new account'(){
        given:
        def account = new Account(handle:'Pres1',email:'George.Washington@whitehouse.gov',password:'Number1President',name:'George Washington')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts',body: json as String,requestContentType: 'application/json')

        then:
        resp.status == 201
        resp.data

        when:
        accountId = resp.data.id

        then:
        accountId
        resp.data.name == 'George Washington'
        resp.data.handle == 'Pres1'
        resp.data.email == 'George.Washington@whitehouse.gov'
        resp.data.password == 'Number1President'
    }

    def 'checks that email is required'(){
        given:
        def account = new Account(handle:'Pres2',password:'Number2President',name:'John Adams')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts',body: json as String,requestContentType: 'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }
}
