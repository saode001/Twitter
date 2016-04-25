package twitter
import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import spock.lang.Ignore
/**
 * Created by Priyanka on 4/19/2016.
 */

@Integration

class UserDetailFunctionalspec extends GebSpec {
    def setup(){
        when:
        go'/'
        waitFor { $("#tUsername").value("Pres1") }
        $("#tPassword").value("Password1")
        $("#tLogin").click()
    }

    def 'U1: User’s detail page will display the user’s name as well as a scrollable list of that user’s postings'(){

        when:
        waitFor { $("#searchTermText").value("John") }
        waitFor { $("#search").click() }

        waitFor { $("#handle1").click() }

        then:
        waitFor { $("#pageHeader").text() == "Pres2's User Details" }
        // User name and email
        $("#userName").text() == "Name: John Adams"
        $("#userEmail").text() == "Email: John.Adams@whitehouse.gov"

        //scrollable list of postings
        $("#postingLabel").text() == "User's Posting:"
        $('#messageDate4').displayed
        $('#msgText4').text() == "Hello Everyone!"
        $('#messageDate3').displayed
        $('#msgText3').text() == "I respond to John, Johnny, or Jojo"
        $('#messageDate2').displayed
        $('#msgText2').text() == "Call me Johnny"
        $('#messageDate1').displayed
        $('#msgText1').text() == "I am John Adams"

    }

    def 'U2: User’s detail page will provide a way for the logged in user to follow the detail user'(){
        when:
        waitFor { $("#searchTermText").value("John") }
        $("#search").click()
        waitFor { $("#handle1").click() }

        then:
        waitFor { $("#pageHeader").text() == "Pres2's User Details" }
        // User name and email
        $("#userName").text() == "Name: John Adams"
        $("#userEmail").text() == "Email: John.Adams@whitehouse.gov"
        $('#followCheck').displayed

        when:
        $('#followCheck').click()
        def alert = waitFor 5, { driver.switchTo().alert() } // click OK to message
        alert.accept()

        then:
        waitFor { !$('#followCheck').displayed }
        waitFor { $('#areFollowing').displayed }

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
        waitFor { $("#tLogout").click() }

        then:
        waitFor { $("#logoutMsg").displayed }
    }

    def 'U3: When the logged in user is following the detail user, the detail page will display a message or icon indicating this'() {
        when:
        waitFor { $("#searchTermText").value("I") }
        $("#search").click()

        waitFor { $("#handle5").click() }

        then:
        waitFor { $("#pageHeader").text() == "Pres3's User Details" }
        // User name and email
        $("#userName").text() == "Name: Thomas Jefferson"
        $("#userEmail").text() == "Email: Thomas.Jefferson@whitehouse.gov"

        $('#followCheck').displayed == false;
        $('#areFollowing').displayed
        $('#areFollowing').text() == "You are following this user"

        //N1: User’s detail page
        waitFor { $("#tCurrentUser").text() == "George Washington" }
        //N2: Search box
        waitFor { $("#searchTermLabel").displayed }
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        waitFor { $('#tLogout').displayed }

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        waitFor { $("#tLogout").click() }

        then:
        waitFor { $("#logoutMsg").displayed }
    }

    def 'U4: When the logged in user goes to their own detail page, they can edit their name and email'() {
        when:
        waitFor { $("#tCurrentUserLink").click() }

        then:
        waitFor { $("#pageHeader").text() == "Pres1's User Details" }
        // User name and email
        $("#userName").text() == "Name: George Washington"
        $("#userEmail").text() == "Email: George.Washington@whitehouse.gov"
        $("#userEdit").displayed

        when:
        $("#edit").click()

        then:
        waitFor { $("#name-input").displayed }
        $("#email-input").displayed

        when:
        $("#name-input").value("G. Washington")
        waitFor { $("#email-input").value("g.washington@whitehouse.gov") }
        waitFor { $("#save").click() }

        then:
        def alert = waitFor 5, { driver.switchTo().alert() } // click OK to message
        alert.accept()

        waitFor { $("#pageHeader").displayed }
        // User name and email
        $("#userName")displayed
        $("#userEmail").displayed
        $("#userEdit").displayed

        when:
        //N2: Search box
        $("#searchTermText").value("John")
        $("#search").click()
        waitFor { $("#handle1").click() }
        waitFor { $("#tCurrentUserLink").click() }

        then:
        waitFor { $("#pageHeader").text() == "Pres1's User Details" }
        // User name and email
        $("#userName").text() == "Name: G. Washington"
        $("#userEmail").text() == "Email: g.washington@whitehouse.gov"
        $("#userEdit").displayed
    }

    def 'Edit user has search, logged in user detail, and logout navigation'(){
        when:
        waitFor { $("#tCurrentUserLink").click() }

        then:
        waitFor { $("#pageHeader").text() == "Pres1's User Details" }
        // User name and email
        $("#userName").text() == "Name: G. Washington"
        $("#userEmail").text() == "Email: g.washington@whitehouse.gov"
        $("#userEdit").displayed

        when:
        $("#edit").click()

        then:
        waitFor { $("#name-input").displayed }
        $("#email-input").displayed

        //N1: User’s detail page
        waitFor { $("#tCurrentUser").text() == "G. Washington" }
        //N2: Search box
        waitFor { $("#searchTermLabel").displayed }
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        waitFor { $('#tLogout').displayed }

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        waitFor { $("#tLogout").click() }

        then:
        waitFor { $("#logoutMsg").displayed }
    }
}
