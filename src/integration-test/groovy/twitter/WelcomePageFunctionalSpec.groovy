package twitter

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import geb.spock.*
import spock.lang.Ignore

@Integration
@Ignore
/**
 * Created by Priyanka on 4/9/2016.
 */
class WelcomePageFunctionalSpec extends GebSpec {

    def 'welcome page displays welcome message'() {
        when:
        go '/'

        then: 'Static welcome displayed properly'
        $('h1').first().text() == 'Welcome to the sample Grails 3 Angular App'

        and: 'Angular generated test displayed properly'
        $('h2').first().text() == 'Hello Stranger'
    }
}
