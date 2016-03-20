package twitter


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
@Ignore
class IntAccountSpec extends Specification {

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


        then:
        account.errors.errorCount == 0
        followAccount1.errors.errorCount == 0
        followAccount2.errors.errorCount == 0
        followAccount3.errors.errorCount == 0
        account.getFollower().size() == 3
        followAccount1.getFollowing().size() == 1
        followAccount2.getFollowing().size() == 1
        followAccount3.getFollowing().size() == 1

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

        then:
        account.errors.errorCount == 0
        followAccount1.errors.errorCount == 0
        account.getFollower().size() == 1
        account.getFollowing().size() == 1
        followAccount1.getFollower().size() == 1
        followAccount1.getFollowing().size() == 1
    }
}
