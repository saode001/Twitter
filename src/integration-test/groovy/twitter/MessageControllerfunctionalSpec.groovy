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
        def account = new Account(handle:'country',email:'country1@yahoo.com',password:'Country1cool',name:'CountryCool')
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
        def resp1 = restClient.post(path:"/accounts/${accountId}/messages",body:message,requestContentType:'application/json')

        then:
        resp1.status == 201
        resp1.data

        resp1.data.messageText == "Test Message"

    }



    /*def 'error response for invalid message text and user'() {
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
        // resp.data.handle == 'country'

         def message = '{"messageText": ""}'
        def resp1 = restClient.post(path:"/accounts/${accountId}/messages",body:message,requestContentType:'application/json')

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 422
    }*/


    def 'show most recent 10 message for an account'() {
        given:
        accountId
     //   def account = new Account(handle: 'Emily', email: 'emily1@yahoo.com', password: 'Emily1cool', name: 'Emmi')
      //  def json = account as JSON
      //  when:
      //  def resp = restClient.post(path: '/accounts', body: json as String, requestContentType: 'application/json')

      //  then:
      //  resp.status == 201
      //  resp.data

        when:
       // accountId = resp.data.id
        // resp.data.handle == 'country'

        def message = '{"messageText": "'+text+'"}'
        def resp2 = restClient.post(path:"/accounts/${accountId}/messages",body:message,requestContentType:'application/json')

        then:
        resp2.status == 201
     //   resp2.data.messageText == "'+text+'"
        resp2.data.id == expectedtId

        where:
        description | text               | expectedtId
       // 'message1'  | "It's message 1"   | 1
        'message2'  | "It's message 2"   | 2
        'message3'  | "It's message 3"   | 3
        'message4'  | "It's message 4"   | 4
        'message5'  | "It's message 5"   | 5
        'message6'  | "It's message 6"   | 6
        'message7'  | "It's message 7"   | 7
        'message8'  | "It's message 8"   | 8
        'message9'  | "It's message 9"   | 9
        'message10' | "It's message 10"  | 10
        'message11' | "It's message 11"  | 11
        'message12' | "It's message 12"  | 12
    }

}

//def cleanup() {
// }

//void "test something"() {
//   when:"The home page is visited"
//       go '/'

//   then:"The title is correct"
//   	$('title').text() == "Welcome to Grails"
//}
