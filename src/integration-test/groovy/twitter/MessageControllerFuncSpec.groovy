package twitter
import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import spock.lang.Ignore

@Integration

class messageControllerFuncSpec extends GebSpec {
    def setup(){
        when:
        go'/'
        sleep(1000)
        $("#tUsername").value("Pres1")
        $("#tPassword").value("Password1")
        $("#tLogin").click()
        sleep(1000)

    }

    def 'R0. Allow for the logged in user to post a new message.'(){

        when:
        $("#newMessage").click()

        then:
        sleep(1000);
        $("#newMessageLabel").text() == "Enter your message:";
        $("#msgText").displayed;

        when:
        $("#msgText").value("Test1");
        $("#createMessage").click();

        then:
        sleep(1000);
        $("#pageHeader").text() == "Pres1's User Details";
        $("#userName").text() == "Name: George Washington";
        $("#userEmail").text() == "Email: George.Washington@whitehouse.gov";
        $("#postingLabel").text() == "User's Posting:";
        $("#msgText7").displayed;
        $("#msgText7").text() == "Test1";
    }

    def 'R1. Use a alert control from the Angular UI library to display an info message saying ‘Message Posted!’.'(){
        when:
        $("#newMessage").click()

        then:
        sleep(1000);
        $("#newMessageLabel").text() == "Enter your message:";
        $("#msgText").displayed;

        when:
        $("#msgText").value("Test2");
        $("#createMessage").click();

        then:
        sleep(1000);
        $("#pageHeader").text() == "Pres1's User Details";
        $("#userName").text() == "Name: George Washington";
        $("#userEmail").text() == "Email: George.Washington@whitehouse.gov";
        $("#postingLabel").text() == "User's Posting:";
        $("#msgText8").displayed;
        $("#msgText8").text() == "Test2";
        page.$('span')[2].text() == "Message Posted!";

        //User’s detail page
        $("#tCurrentUser").text() == "George Washington"
        //Search box
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        //Logout
        $('#tLogout').displayed

        when:
        //Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        sleep(1000)
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }

    def 'R2. Use Angular validation to validate a message prior to posting it to the server via the REST API (client side validation)'() {
        when:
        $("#newMessage").click()

        then:
        sleep(1000);
        $("#newMessageLabel").text() == "Enter your message:";
        $("#msgText").displayed;

        when:
        $("#msgText").value("");
        $("#createMessage").click();

        then:
        sleep(1000);
        $("#requiredError").displayed

        when:
        $("#msgText").value("This is a message that is over forty characters.  This message is too long.");

        then:
        sleep(1000);
        $("#lengthError").displayed

        //User’s detail page
        $("#tCurrentUser").text() == "George Washington"
        //Search box
        $("#searchTermLabel").displayed
        $("#searchTermText").displayed
        $("#search").displayed
        //Logout
        $('#tLogout').displayed

        when:
        //Logout - clicking this should bring you to the login screen and provide a helpful message ‘Sorry to see you go… etc’
        sleep(1000)
        $("#tLogout").click()

        then:
        sleep(1000)
        $("#logoutMsg").displayed
    }
}
