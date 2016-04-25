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
        waitFor { $("#tUsername").value("Pres1") }
        $("#tPassword").value("Password1")
        $("#tLogin").click()

        then:
        waitFor { $("#tLogout").displayed }
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed

        //N1: User’s detail page
        waitFor { $("#tCurrentUser").text() == "George Washington" }
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
        waitFor { $("#logoutMsg").displayed }
    }

    def 'S2: Display matching results in a scrollable area below the search box'() {
        when:
        go '/'
        waitFor { $("#tUsername").value("Pres1") }
        $("#tPassword").value("Password1")
        $("#tLogin").click()

        waitFor { $("#searchTermText").value("John") }
        $("#search").click()

        then:
        waitFor { $("#searchResults").displayed }
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
        waitFor { $("#logoutMsg").displayed }
    }

    def 'S3: Search result messages will display the message contents as well as the posting user' () {
        when:
        go '/'
        waitFor { $("#tUsername").value("Pres1") }
        $("#tPassword").value("Password1")
        $("#tLogin").click()

        waitFor { $("#searchTermText").value("John") }
        $("#search").click()

        then:
        waitFor { $("#searchResults").displayed }
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
        waitFor { $("#logoutMsg").displayed }
    }

    def 'S4: Clicking on the posting user’s name in a message will route to the user’s detail page' (){
        when:
        go '/'
        waitFor { $("#tUsername").value("Pres1") }
        $("#tPassword").value("Password1")
        $("#tLogin").click()

        waitFor { $("#searchTermText").value("John") }
        $("#search").click()

        waitFor { $("#handle1").click() }

        then:
        waitFor { $("#pageHeader").text() == "Pres2's User Details" }

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
        waitFor { $("#logoutMsg").displayed }
    }
}
