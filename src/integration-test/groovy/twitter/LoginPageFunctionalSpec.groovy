package twitter

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import geb.spock.*
import spock.lang.Ignore

@Integration
@Ignore
class LoginPageFunctionalSpec extends GebSpec {

    def 'L1: When not logged in, route user to the login screen'() {
        when:
        go '/'

        then:
        $("#tUsername").displayed
        $("#tPassword").displayed

    }

    def 'L2: Login screen allows a user to enter username and password to gain access' () {
        when:
        go '/'
        sleep(1000)
        $("#tUsername").value("Pres1")
        sleep(1000)
        $("#tPassword").value("Password1")
        sleep(1000)
        $("#tLogin").click()
        sleep(1000)

        then:
        $('#tLogout').displayed
    }

    def 'L3: Invalid login will be rejected with an error message' () {
        when:
        go '/'
        sleep(1000)
        $("#tUsername").value("George")
        sleep(1000)
        $("#tPassword").value("Washtington")
        sleep(1000)
        $("#tLogin").click()
        sleep(1000)

        then:
        $("#loginError").text() == 'Invalid login'
        $("#tUsername").displayed
        $("#tPassword").displayed
    }
}
