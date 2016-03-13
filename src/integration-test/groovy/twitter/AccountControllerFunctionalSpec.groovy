package twitter

import geb.spock.GebSpec
import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise


@Integration
@Stepwise
class AccountControllerFunctionalSpec extends GebSpec {

    @Shared
    def accountId

    RESTClient restClient

    def setup() {
        restClient = new RESTClient(baseUrl)
    }

    def 'get all accounts'() {
        when:
        def resp = restClient.get(path: '/accounts')

        then:
        resp.status == 200
        resp.data.size() == 0
    }

    def 'saves a new account'() {
        given:
        def account = new Account(handle: 'Pres1', email: 'George.Washington@whitehouse.gov', password: 'Number1President', name: 'George Washington')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

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

    def 'checks that email is required'() {
        given:
        def account = new Account(handle: 'Pres2', password: 'Number2President', name: 'John Adams')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }

    def 'checks that handle is required'() {
        given:
        def account = new Account(email: 'John.Adams@whitehouse.gov', password: 'Number2President',name:'John Adams')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts',body: json as String, requestContentType: 'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }

    def 'checks that password is required'() {
        given:
        def account = new Account(handle: 'Pres2', email: 'John.Adams@whitehouse.gov',name: 'John Adams')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts',body: json as String,requestContentType: 'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }

    def 'checks that name is required'() {
        given:
        def account = new Account(handle: 'Pres2', email: 'John.Adams@whitehouse.gov', password: 'Number2President')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts',body: json as String,requestContentType: 'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }

    def 'gets a new account'() {
        when:
        def resp = restClient.get(path: "/accounts/${accountId}")

        then:
        resp.status == 200
        resp.data.id == accountId
        resp.data.handle == 'Pres1'
        resp.data.email == 'George.Washington@whitehouse.gov'
        resp.data.password == 'Number1President'
        resp.data.name == 'George Washington'
    }

    def 'updates an existing account'(){
        given:
        def account = new Account(id: 1, handle:'FirstPresident', email: 'George.Washington@whitehouse.gov', password: 'Number1President', name: 'George Washington')
        def json = account as JSON

        when:
        def resp = restClient.put(path: "/accounts/${accountId}",body: json as String,requestContentType: 'application/json')

        then:
        resp.status == 200

        when:
        resp = restClient.get(path: "/accounts/${accountId}")

        then:
        resp.status == 200
        resp.data.handle == 'FirstPresident'
    }

    def 'check that an account is deleted'() {
        when:
        def resp = restClient.delete(path: "/accounts/${accountId}")

        then:
        resp.status == 204

        when:
        restClient.get(path: "/accounts/${accountId}")

        then:
        thrown(HttpResponseException)

        when:
        resp = restClient.get(path: '/accounts')

        then:
        resp.status == 200
        resp.data.size() == 0
    }

    def 'check that we can retrieve accounts by id or handle'() {
        given:
        def account = new Account(handle: handle, email: email, password: 'TestPassword1', name: name)
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

        then:
        resp.status == 201

        when:
        def restCall = '/accounts/'+ RowNum
        resp = restClient.get(path: restCall)

        then:
        resp.status == 200
        resp.data.name == name
        resp.data.handle == handle
        resp.data.id == RowNum

        when:
        restCall = '/accounts/'+ handle
        resp = restClient.get(path: restCall)

        then:
        resp.status == 200
        resp.data.name == name
        resp.data.handle == handle
        resp.data.id == RowNum

        where:
        description            | handle  | email                             | name               | RowNum
        'get Thomas Jefferson' | 'Pres3' | 'Thomas.Jefferson@whitehouse.gov' | 'Thomas Jefferson' | 2
        'get James Madison'    | 'Pres4' | 'James.Madison@whitehouse.gov'    | 'James Madison'    | 3
        'get James Monroe'     | 'Pres5' | 'James.Monroe@whitehouse.gov'     | 'James Monroe'     | 4
    }

    def 'check that two accounts can follow each other'() {
        given:
        def account = new Account(handle: 'Pres1',email: 'George.Washington@whitehoouse.gov',password: 'Number1President',name: 'George Washington')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts',body: json as String,requestContentType: 'application/json')

        then:
        resp.status == 201
        def followerID = resp.data.id

        when:
        account = new Account(handle: 'Pres2',email: 'John.Adams@whitehouse.gov',password: 'Number2President',name: 'John Adams')
        json = account as JSON
        resp = restClient.post(path: '/accounts',body: json as String,requestContentType: 'application/json')

        then:
        resp.status == 201
        def followeeID = resp.data.id

        when:
        resp = restClient.get(path: "/accounts/${followeeID}/follow/${followerID}")

        then:
        resp.status == 200
        resp.data.id == followeeID
        resp.data.following.id[0] == followerID

    }
}