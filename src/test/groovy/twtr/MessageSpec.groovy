package twtr

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Message)
class MessageSpec extends Specification {
    def 'Create new Message with a valid account'() {
        given:
        def user = new Account(handle: 'Kelly',email: 'schra435@umn.edu',password: 'Test12345',name: 'Kelly Schrader')
        def message = new Message(messageText: 'This is a test message.', account: user)

        when:
        message.save()

        then:
        message.id
        !message.hasErrors()
    }

    def 'fails to save when required fields are missing'() {
        given:
        def user = new Account(handle: 'Kelly',email: 'schra435@umn.edu',password: 'Test12345',name: 'Kelly Schrader')
        def message = new Message(messageText: 'This is a test message.')

        when:
        message.save()

        then:
        !message.id
        message.hasErrors()
        message.errors.getFieldError('account')
    }

    def 'test message constrains' () {
        given:
        def user = new Account(handle: 'Kelly',email: 'schra435@umn.edu',password: 'Test12345',name: 'Kelly Schrader')
        def message = new Message(messageText: messageText, account: user)

        when:
        message.save()

        then:
        message.errors.errorCount == expectedResults

        where:
        description                     |   messageText                                       |   expectedResults
        'Valid message'                 |   'This is a test message'                          |   0
        'Blank message'                 |   ''                                                |   1
        'Message over 40 characters'    |   'This is a message that is over 40 characters'    |   1
    }
}
