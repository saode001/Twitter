package twtr

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Message)
class MessageSpec extends Specification {
    def startingMessageCount

    def setup() {
        startingMessageCount = Message.count()
    }

    def 'Create new Message with a valid account'() {
        given:
        def user = new Account(handle: 'Kelly',email: 'schra435@umn.edu',password: 'Test12345',name: 'Kelly Schrader')
        def message = new Message(messageText: 'This is a test message.', account: user)

        when:
        message.save()

        then:
        message.id
        !message.hasErrors()
        Message.get(message.id)
        Message.count() == startingMessageCount + 1
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
        Message.count() == startingMessageCount
    }

    def 'fails to save with bad mssage text' () {
        given:
        def user = new Account(handle: 'Kelly',email: 'schra435@umn.edu',password: 'Test12345',name: 'Kelly Schrader')
        def message = new Message(messageText: messageText, account: user)

        when:
        message.save()

        then:
        message.hasErrors()
        !message.id
        Message.count() == startingMessageCount

        where:
        description                     |   messageText
        'Null message'                  |   null
        'Blank message'                 |   ''
        'Message over 40 characters'    |   '1'*41
    }
}
