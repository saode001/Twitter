package twitter
import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import spock.lang.Ignore

import java.text.SimpleDateFormat

/**
 * Created by Priyanka on 4/19/2016.
 */

@Integration

class userDetailFunctionalspec extends GebSpec {
    def setup(){
        when:
        go'/'
        sleep(1000)
        $("#tUsername").value("Pres1")
        $("#tPassword").value("Password1")
        $("#tLogin").click()
        sleep(1000)

    }


    def 'U1 and R5: User’s detail page will display the user’s name as well as a scrollable list of that user’s postings'(){

        when:
        $("#searchTermText").value("John")
        sleep(1000)
        $("#search").click()

        sleep(1000)
        $("#handle1").click()

        then:
        sleep(1000)
        $("#pageHeader").text() == "Pres2's User Details"
        // User name and email
        $("#userName").text() == "Name: John Adams"
        $("#userEmail").text() == "Email: John.Adams@whitehouse.gov"

        //scrollable list of postings
        $("#postingLabel").text() == "User's Posting:"
        $("#messageDate4").text()== "May 6"
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
        $("#searchTermText").value("John")
        sleep(1000)
        $("#search").click()
        sleep(1000)
        $("#handle1").click()

        then:
        sleep(1000)
        $("#pageHeader").text() == "Pres2's User Details"
        // User name and email
        $("#userName").text() == "Name: John Adams"
        $("#userEmail").text() == "Email: John.Adams@whitehouse.gov"
        $('#followCheck').displayed

        when:
        $('#followCheck').click()
        sleep(1000)
        driver.switchTo().alert().accept() // click OK to message

        then:
        sleep(1000)
        $('#followCheck').displayed == false;
        $('#areFollowing').displayed

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
        sleep(1000)
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }

    def 'U3: When the logged in user is following the detail user, the detail page will display a message or icon indicating this'() {
        when:
        $("#searchTermText").value("I")
        sleep(1000)
        $("#search").click()

        sleep(1000)
        $("#handle5").click()

        then:
        sleep(1000)
        $("#pageHeader").text() == "Pres3's User Details"
        // User name and email
        $("#userName").text() == "Name: Thomas Jefferson"
        $("#userEmail").text() == "Email: Thomas.Jefferson@whitehouse.gov"

        $('#followCheck').displayed == false;
        $('#areFollowing').displayed
        $('#areFollowing').text() == "You are following this user"

        //N1: User’s detail page
        sleep(1000)
        $("#tCurrentUser").text() == "George Washington"
        //N2: Search box
        sleep(1000)
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        sleep(1000)
        $('#tLogout').displayed

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        sleep(1000)
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }

    def 'U4: When the logged in user goes to their own detail page, they can edit their name and email'() {
        when:
        sleep(1000)
        $("#tCurrentUserLink").click()

        then:
        sleep(1000)
        $("#pageHeader").text() == "Pres1's User Details"
        // User name and email
        $("#userName").text() == "Name: George Washington"
        $("#userEmail").text() == "Email: George.Washington@whitehouse.gov"
        $("#userEdit").displayed

        when:
        $("#edit").click()

        then:
        sleep(1000)
        $("#name-input").displayed
        $("#email-input").displayed

        when:
        $("#name-input").value("G. Washington")
        sleep(1000)
        $("#email-input").value("g.washington@whitehouse.gov")
        sleep(1000)
        $("#save").click()

        then:
        sleep(1000)
        driver.switchTo().alert().accept() // click OK to message
        sleep(1000)
        $("#pageHeader").displayed
        // User name and email
        $("#userName")displayed
        $("#userEmail").displayed
        $("#userEdit").displayed

        when:
        //N2: Search box
        $("#searchTermText").value("John")
        $("#search").click()
        sleep(1000)
        $("#handle1").click()
        sleep(1000)
        $("#tCurrentUserLink").click()

        then:
        sleep(1000)
        $("#pageHeader").text() == "Pres1's User Details"
        // User name and email
        $("#userName").text() == "Name: G. Washington"
        $("#userEmail").text() == "Email: g.washington@whitehouse.gov"
        $("#userEdit").displayed
    }

    def 'Edit user has search, logged in user detail, and logout navigation'(){
        when:
        sleep(1000)
        $("#tCurrentUserLink").click()

        then:
        sleep(1000)
        $("#pageHeader").text() == "Pres1's User Details"
        // User name and email
        $("#userName").text() == "Name: G. Washington"
        $("#userEmail").text() == "Email: g.washington@whitehouse.gov"
        $("#userEdit").displayed

        when:
        $("#edit").click()

        then:
        sleep(1000)
        $("#name-input").displayed
        $("#email-input").displayed

        //N1: User’s detail page
        sleep(1000)
        $("#tCurrentUser").text() == "G. Washington"
        //N2: Search box
        sleep(1000)
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        // N3: Logout
        sleep(1000)
        $('#tLogout').displayed

        when:
        //N3: Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        sleep(1000)
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }
}
