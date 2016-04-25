package twitter

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import geb.spock.*
import spock.lang.Ignore

@Integration
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

        waitFor { $("#tUsername").value("Pres1") }
        waitFor { $("#tPassword").value("Password1") }
        $("#tLogin").click()

        then:
        waitFor { $('#tLogout').displayed }
    }

    def 'L3: Invalid login will be rejected with an error message' () {
        when:
        go '/'
        $("#tUsername").value("George")
        $("#tPassword").value("Washtington")
        $("#tLogin").click()

        then:
        waitFor { $("#loginError").text() == 'Invalid login' }
        $("#tUsername").displayed
        $("#tPassword").displayed
    }
}
