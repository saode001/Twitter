package twitter

import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import spock.lang.*
import geb.spock.*

import javax.xml.soap.Text

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Stepwise
@Ignore
//@Rollback
class MessageControllerFunctionalSpec extends GebSpec {
  @Shared
  def accountId

  RESTClient restClient

  def setup() {
    restClient = new RESTClient(baseUrl)
  }

  def 'create a new message'() {
    given:
    def account = new Account(handle: 'country', email: 'country1@yahoo.com', password: 'Country1cool', name: 'CountryCool')
    def json = account as JSON

    when:
    def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

    then:
    resp.status == 201
    resp.data

    when:
    accountId = resp.data.id
    resp.data.handle == 'country'

    def message = '{"messageText": "Test Message"}'
    def resp1 = restClient.post(path: "/accounts/${accountId}/messages", body: message, requestContentType: 'application/json')

    then:
    resp1.status == 201
    resp1.data

    resp1.data.messageText == "Test Message"

  }


  def 'error response for invalid message text and user'() {
    given:
    def account = new Account(handle: 'priyanka', email: 'priyanka1@yahoo.com', password: 'Priyanka1cool', name: 'Piku')
    def json = account as JSON
    when:
    def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

    then:
    resp.status == 201
    resp.data

    when:
    accountId = resp.data.id

    def message = '{"messageText": ""}'
    def resp1 = restClient.post(path: "/accounts/${accountId}/messages", body: message, requestContentType: 'application/json')

    then:
    def e = thrown(HttpResponseException)
    e.response.status == 422
  }

  def 'show most recent 10 message for an account'() {

    when: 'Create 10 messages 2 - 12'
    def responses = []
    (2..20).each {
      def message = ([messageText: "It's message ${it}"] as JSON) as String
      responses << restClient.post(path: "/accounts/${accountId}/messages", body: message, requestContentType: 'application/json')
    }

    then: 'make sure all the responses are valid creates'
    responses.each { response ->
      assert response.status == 201
    }

    when: 'get messages for account, with no limit should only get 10'
    def resp3 = restClient.get(path: "/accounts/${accountId}/messages")
    def messages = resp3.data as List

    then: 'request should succeed'
    resp3.status == 200

    and: 'should get back 10 messages'
    messages.size() == 10

    and: 'should get messages from 20 to 11'
    messages[0].messageText == "It's message 20"
    messages[9].messageText == "It's message 11"

    when: 'get messages for account, limit to 3 and skip the first one'
    def resp4 = restClient.get(path: "/accounts/${accountId}/messages", query: [offset:1, max: 3])
    messages = resp4.data as List

    then: 'request should succeed'
    resp4.status == 200

    and: 'should get back 3 messages'
    messages.size() == 3

    and: 'messages should be 19, 18, 17 (skipping 20)'
    messages[0].messageText == "It's message 19"
    messages[1].messageText == "It's message 18"
    messages[2].messageText == "It's message 17"
  }

}

