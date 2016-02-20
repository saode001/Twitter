package twtr


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class IntAccountSpec extends Specification {

    def startingAccounts

    def setup() {
        startingAccounts = Account.count()
    }

    def 'create new account' () {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password:'Test1234', name: 'Kelly Schrader')

        when:
        account.save()

        then:
        account.errors.errorCount == 0
    }

    def 'create account with same email should fail' () {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password:'Test1234', name: 'Kelly Schrader')
        def dupAccount = new Account(handle: 'KellyDuplicate', email: 'schra435@umn.edu',password: 'Test1234',name:'Kelly Schrader')

        when:
        account.save()
        dupAccount.save()

        then:
        account.errors.errorCount == 0
        dupAccount.errors.errorCount == 1
        dupAccount.errors.getFieldError('email')
        Account.count() == startingAccounts + 1
    }

    def 'create account with same handle should fail' () {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password:'Test1234', name: 'Kelly Schrader')
        def dupAccount = new Account(handle: 'Kelly', email: 'kschrade@yahoo.com',password: 'Test1234',name:'Kelly Schrader')

        when:
        account.save()
        dupAccount.save()

        then:
        account.errors.errorCount == 0
        dupAccount.errors.errorCount == 1
        dupAccount.errors.getFieldError('handle')
        Account.count() == startingAccounts + 1
    }

    def 'updating user to have same handle and email should fail' () {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password:'Test1234', name: 'Kelly Schrader')
        def dupAccount = new Account(handle: 'KellyDuplicate', email: 'kschrade@yahoo.com',password: 'Test1234',name:'Kelly Schrader')

        when:
        account.save()
        dupAccount.save()
        dupAccount.handle = 'Kelly'
        dupAccount.email = 'schra435@umn.edu'
        dupAccount.save()

        then:
        account.errors.errorCount == 0
        dupAccount.errors.errorCount == 2
        dupAccount.errors.getFieldError('email')
        dupAccount.errors.getFieldError('handle')
        Account.count() == startingAccounts + 2
    }

    def 'account can have mulitple followers' () {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password:'Test1234', name: 'Kelly Schrader')
        def followAccount1 = new Account(handle: 'Follower1',email: 'follower1@gmail.com',password: 'Test12345',name: 'Amy Follower',following:account)
        def followAccount2 = new Account(handle: 'Follower2',email: 'follower2@gmail.com',password: 'Test12345',name: 'Tom Follower',following:account)
        def followAccount3 = new Account(handle: 'Follower3',email: 'follower3@gmail.com',password: 'Test12345',name: 'Suzy Follower',following: account)

        when:
        account.save()
        followAccount1.save()
        followAccount2.save()
        followAccount3.save()

        account.addToFollower(followAccount1)
        account.addToFollower(followAccount2)
        account.addToFollower(followAccount3)
        account.save()

        and:
        def fetchedAccount = Account.get(account.id)

        then:
        fetchedAccount.follower.size() == 3
        fetchedAccount.follower.find { it.id == followAccount1.id }
        fetchedAccount.follower.find { it.id == followAccount2.id }
        fetchedAccount.follower.find { it.id == followAccount3.id }
    }

    def 'Two accounts can follow each other'() {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password:'Test1234', name: 'Kelly Schrader')
        def followAccount1 = new Account(handle: 'Follower1',email: 'follower1@gmail.com',password: 'Test12345',name: 'Amy Follower',following:account)

        when:
        account.save()
        followAccount1.save()

        account.addToFollower(followAccount1)
        account.addToFollowing(followAccount1)
        account.save()

        followAccount1.addToFollower(account)
        followAccount1.save()

        and:
        def fetchedAccount = Account.get(account.id)
        def fetchedFollowAccount1 = Account.get(followAccount1.id)

        then:
        fetchedAccount.follower.size() == 1
        fetchedAccount.follower[0].id == fetchedFollowAccount1.id
        fetchedAccount.following.size() == 1
        fetchedAccount.following[0].id == fetchedFollowAccount1.id

        and:
        fetchedFollowAccount1.follower.size() == 1
        fetchedFollowAccount1.follower[0].id == fetchedAccount.id
        fetchedFollowAccount1.following.size() == 1
        fetchedFollowAccount1.following[0].id == fetchedAccount.id
    }
}
