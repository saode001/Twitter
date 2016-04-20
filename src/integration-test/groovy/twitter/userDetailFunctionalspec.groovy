package twitter
import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import spock.lang.Ignore
/**
 * Created by Priyanka on 4/19/2016.
 */

@Integration

class userDetailFunctionalspec extends GebSpec {
    def setup(){
        when:
        go'/'
        $("# input[Username]").value("Pres1")
        $("# input[Password]").value("Password1")
        $("#login").click()
        sleep(1000)

    }


    def 'U1: User’s detail page will display the user’s name as well as a scrollable list of that user’s postings'(){


        when:
        $("#userDetails").click()
        sleep(2000)


        then:
        $("div", Username:"John Adams")
        $("div", Email:"John.Adams@whitehouse.gov")
        $("div", Message: "Hello Everyone!")
        $("div", Message: " I respond to John, Johnny, or Jojo")
        $("div", Message: "Call me Johnny!")


    }
}
