package twitter

import grails.test.mixin.integration.Integration
import grails.transaction.*

import spock.lang.*
import geb.spock.*

@Integration
@Rollback
class SearchPageFunctionalSpec extends GebSpec {

    def 'S1: Provide a search box for finding messages by message poster and message contents'() {
        when:
        go '/'
        sleep(1000)
        $("#tUsername").value("Pres1")
        sleep(1000)
        $("#tPassword").value("Password1")
        sleep(1000)
        $("#tLogin").click()

        then:
        sleep(1000)
        $("#tLogout").displayed
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed

        //N1: User’s detail page
        $("#tCurrentUser").text() == "George Washington"
        //N2: Search box
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        $('#tLogout').displayed

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }

    def 'S2: Display matching results in a scrollable area below the search box'() {
        when:
        go '/'
        sleep(1000)
        $("#tUsername").value("Pres1")
        sleep(1000)
        $("#tPassword").value("Password1")
        sleep(1000)
        $("#tLogin").click()

        sleep(1000)
        $("#searchTermText").value("John")
        sleep(1000)
        $("#search").click()

        then:
        sleep(1000)
        $("#searchResults").displayed
        $("#handle1").text() == "Pres2"
        $("#handle2").text() == "Pres2"
        $("#handle3").text() == "Pres2"

        //N1: User’s detail page
        $("#tCurrentUser").text() == "George Washington"
        //N2: Search box
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        $('#tLogout').displayed

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }

    def 'S3: Search result messages will display the message contents as well as the posting user' () {
        when:
        go '/'
        sleep(1000)
        $("#tUsername").value("Pres1")
        sleep(1000)
        $("#tPassword").value("Password1")
        sleep(1000)
        $("#tLogin").click()

        sleep(1000)
        $("#searchTermText").value("John")
        sleep(1000)
        $("#search").click()

        then:
        sleep(1000)
        $("#searchResults").displayed
        $("#handle1").text() == "Pres2"
        $("#message1",text:iContains("I am John Adams")).size() == 1
        $("#handle2").text() == "Pres2"
        $("#message2",text:iContains("Call me Johnny")).size() == 1
        $("#handle3").text() == "Pres2"
        $("#message3",text:iContains("I respond to John, Johnny, or Jojo")).size() == 1

        //N1: User’s detail page
        $("#tCurrentUser").text() == "George Washington"
        //N2: Search box
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        $('#tLogout').displayed

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }

    def 'S4: Clicking on the posting user’s name in a message will route to the user’s detail page' (){
        when:
        go '/'
        sleep(1000)
        $("#tUsername").value("Pres1")
        sleep(1000)
        $("#tPassword").value("Password1")
        sleep(1000)
        $("#tLogin").click()

        sleep(1000)
        $("#searchTermText").value("John")
        sleep(1000)
        $("#search").click()

        sleep(1000)
        $("#handle1").click()

        then:
        sleep(1000)
        $("#pageHeader").text() == "Pres2's User Details"

        //N1: User’s detail page
        $("#tCurrentUser").text() == "George Washington"
        //N2: Search box
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        $('#tLogout').displayed

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }
}
