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
        def account = new Account(email: 'John.Adams@whitehouse.gov', password: 'Number2President', name: 'John Adams')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }

    def 'checks that password is required'() {
        given:
        def account = new Account(handle: 'Pres2', email: 'John.Adams@whitehouse.gov', name: 'John Adams')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }

    def 'checks that name is required'() {
        given:
        def account = new Account(handle: 'Pres2', email: 'John.Adams@whitehouse.gov', password: 'Number2President')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

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

    def 'updates an existing account'() {
        given:
        def account = new Account(id: 1, handle: 'FirstPresident', email: 'George.Washington@whitehouse.gov', password: 'Number1President', name: 'George Washington')
        def json = account as JSON

        when:
        def resp = restClient.put(path: "/accounts/${accountId}", body: json as String, requestContentType: 'application/json')

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
        def restCall = '/accounts/' + RowNum
        resp = restClient.get(path: restCall)

        then:
        resp.status == 200
        resp.data.name == name
        resp.data.handle == handle
        resp.data.id == RowNum

        when:
        restCall = '/accounts/' + handle
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
        def account = new Account(handle: 'Pres1', email: 'George.Washington@whitehoouse.gov', password: 'Number1President', name: 'George Washington')
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

        then:
        resp.status == 201
        def followerID = resp.data.id

        when:
        account = new Account(handle: 'Pres2', email: 'John.Adams@whitehouse.gov', password: 'Number2President', name: 'John Adams')
        json = account as JSON
        resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

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

    def 'check for follower and following count'() {
        given:
        def resp = restClient.get(path: '/accounts/Pres1')
        accountId = resp.data.id

        def account = new Account(handle: 'Pres6', email: 'John.Quincy.Adams@whitehouse.gov', password: 'Number6President', name: 'John Quincy Adams')
        def json = account as JSON
        resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')
        def followeeId = resp.data.id
        resp = restClient.get(path: "/accounts/${followeeId}/follow/${accountId}")

        account = new Account(handle: 'Pres7', email: 'Andrew.Jackson@whitehouse.gov', password: 'Number7President', name: 'Andrew Jackson')
        json = account as JSON
        resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')
        followeeId = resp.data.id
        resp = restClient.get(path: "/accounts/${followeeId}/follow/${accountId}")

        account = new Account(handle: 'Pres8', email: 'Martin.Van.Buren@whitehouse.gov', password: 'Number8President', name: 'Martin Van Buren')
        json = account as JSON
        resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')
        followeeId = resp.data.id
        resp = restClient.get(path: "/accounts/${followeeId}/follow/${accountId}")

        account = new Account(handle: 'Pres9', email: 'William.Henry.Harrison@whitehouse.gov', password: 'Number9President', name: 'William Henry Harrison')
        json = account as JSON
        resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')
        followeeId = resp.data.id
        resp = restClient.get(path: "/accounts/${followeeId}/follow/${accountId}")

        when:
        def resp1 = restClient.get(path: "/accounts/${accountId}")

        then:
        resp1.status == 200
        resp1.data.numFollowers == 5
        resp1.data.numFollowing == 0

        when:
        def resp2 = restClient.get(path: '/accounts/Pres8')

        then:
        resp2.status == 200
        resp2.data.numFollowers == 0
        resp2.data.numFollowing == 1
    }

    def "test max of 10 and offset"() {
        given:
        def account = new Account(handle: handle, email: email, password: 'TestPassword1', name: name)
        def json = account as JSON

        when:
        def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')
        def followeeId = resp.data.id

        then:
        resp.status == 201

        when:
        resp = restClient.get(path: '/accounts/Pres1')
        def followId = resp.data.id

        resp = restClient.get(path: "/accounts/${followeeId}/follow/${followId}")

        then:
        resp.status == 200

        when:
        resp = restClient.get(path: "/accounts/${followId}")

        then:
        resp.status == 200
        resp.data.numFollowers == numFollowers

        when:
        resp = restClient.get(path: "/accounts/${followId}/followers?max=10&offset=${offset}")

        then:
        resp.data.followers.size() == 5


        where:
        description                 | handle   | email                                 | name                    | numFollowers | offset
        'Add John Tyler'            | 'Pres10' | 'John.Tyler@whitehouse.gov'           | 'John Tyler'            | 6            | 0
        'Add James K. Polk'         | 'Pres11' | 'James.K.Polk@whitehouse.gov'         | 'James K. Polk'         | 7            | 0
        'Add Zachary Taylor'        | 'Pres12' | 'Zachary.Taylor@whitehouse.gov'       | 'Zachary Taylor'        | 8            | 0
        'Add Millard Fillmore'      | 'Pres13' | 'Millard.Fillmore@whitehouse.gov'     | 'Millard Fillmore'      | 9            | 0
        'Add Franklin Pierce'       | 'Pres14' | 'Franklin.Pierce@whitehouse.gov'      | 'Franklin Pierce'       | 10           | 0
        'Add James Buchanan'        | 'Pres15' | 'James.Buchanan@whitehouse.gov'       | 'James Buchanan'        | 11           | 1
        'Add Abraham Lincoln'       | 'Pres16' | 'Abraham.Lincoln@whitehouse.gov'      | 'Abraham Lincoln'       | 12           | 1
        'Add Andrew Johnson'        | 'Pres17' | 'Andrew.Johnson@whitehouse.gov'       | 'Andrew Johnson'        | 13           | 1
        'Add Ulysses S. Grant'      | 'Pres18' | 'Ulysses.S.Grant@whitehouse.gov'      | 'Ulysses S. Grant'      | 14           | 1
        'Add Rutherford B. Hayes'   | 'Pres19' | 'Rutherford.B.Hayes@whitehouse.gov'   | 'Rutherford B. Hayes'   | 15           | 1
        'Add James A. Garfield'     | 'Pres20' | 'James.A.Garfield@whitehouse.gov'     | 'James A. Garfield'     | 16           | 1
        'Add Chester Arthur'        | 'Pres21' | 'Chester.Arthur@whitehouse.gov'       | 'Chester Arthur'        | 17           | 1
        'Add Grover Cleveland'      | 'Pres22' | 'Grover.Cleveland@whitehouse.gov'     | 'Grover Cleveland'      | 18           | 1
        'Add Benjamin Harrison'     | 'Pres23' | 'Benjamin.Harrison@whitehouse.gov'    | 'Benjamin Harrison'     | 19           | 1
        'Add Grover Cleveland'      | 'Pres24' | 'Grover.Cleveland@whitehouse.gov'     | 'Grover Cleveland'      | 20           | 1
        'Add William McKinley'      | 'Pres25' | 'William.McKinley@whitehouse.gov'     | 'William McKinley'      | 21           | 2
        'Add Theodore Roosevelt'    | 'Pres26' | 'Theodore.Roosevelt@whitehouse.gov'   | 'Theodore Roosevelt'    | 22           | 2
        'Add William Howard Taft'   | 'Pres27' | 'William.Howard.Taft@whitehouse.gov'  | 'William Howard Taft'   | 23           | 2
        'Add Woodrow Wilson'        | 'Pres28' | 'Woodrow.Wilson@whitehouse.gov'       | 'Woodrow Wilson'        | 24           | 2
        'Add Warren G. Harding'     | 'Pres29' | 'Warren.G.Harding@whitehouse.gov'     | 'Warren G. Harding'     | 25           | 2
        'Add Calvin Coolidge'       | 'Pres30' | 'Calvin.Coolidge@whitehouse.gov'      | 'Calvin Coolidge'       | 26           | 2
        'Add Herbert Hoover'        | 'Pres31' | 'Herbert.Hoover@whitehouse.gov'       | 'Herbert Hoover'        | 27           | 2
        'Add Franklin D. Roosevelt' | 'Pres32' | 'Franklin.D.Roosevelt@whitehouse.gov' | 'Franklin D. Roosevelt' | 28           | 2
        'Add Harry S. Truman'       | 'Pres33' | 'Harry.S.Truman@whitehouse.gov'       | 'Harry S. Truman'       | 29           | 2
        'Add Dwight D. Eisenhower'  | 'Pres34' | 'Dwight.D.Eisenhower@whitehouse.gov'  | 'Dwight D. Eisenhower'  | 30           | 2
        'Add John F. Kennedy'       | 'Pres35' | 'John.F.Kennedy@whitehouse.gov'       | 'John F. Kennedy'       | 31           | 3
        'Add Lyndon B. Johnson'     | 'Pres36' | 'Lyndon.B.Johnson@whitehouse.gov'     | 'Lyndon B. Johnson'     | 32           | 3
        'Add Richard Nixon'         | 'Pres37' | 'Richard.Nixon@whitehouse.gov'        | 'Richard Nixon'         | 33           | 3
        'Add Gerald Ford'           | 'Pres38' | 'Gerald.Ford@whitehouse.gov'          | 'Gerald Ford'           | 34           | 3
        'Add Jimmy Carter'          | 'Pres39' | 'Jimmy.Carter@whitehouse.gov'         | 'Jimmy Carter'          | 35           | 3
        'Add Ronald Reagan'         | 'Pres40' | 'Ronald.Reagan@whitehouse.gov'        | 'Ronald Reagan'         | 36           | 3
        'Add George Bush'           | 'Pres41' | 'George.Bush@whitehouse.gov'          | 'George Bush'           | 37           | 3
        'Add Bill Clinton'          | 'Pres42' | 'Bill.Clinton@whitehouse.gov'         | 'Bill Clinton'          | 38           | 3
        'Add George W. Bush'        | 'Pres43' | 'George.W.Bush@whitehouse.gov'        | 'George W. Bush'        | 39           | 3
        'Add Barack Obama'          | 'Pres44' | 'Barack.Obama@whitehouse.gov'         | 'Barack Obama'          | 40           | 3
    }
}