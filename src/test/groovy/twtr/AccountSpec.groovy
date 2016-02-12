package twtr

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Account)
class AccountSpec extends Specification {

    def 'saving account persists new account in database'() {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password: 'Test1234', name: 'Kelly Schrader')

        when:
        account.save(failOnError: true)

        then:
        account.errors.errorCount == 0
        account.id
        Account.get(account.id).email == 'schra435@umn.edu'
        Account.get(account.id).handle == 'Kelly'
        Account.get(account.id).password == 'Test1234'
        Account.get(account.id).name == 'Kelly Schrader'
    }
}
