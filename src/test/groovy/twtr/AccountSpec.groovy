package twtr

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Account)
@Unroll
class AccountSpec extends Specification {

    def startingNumberOfAccounts

    def setup() {
        startingNumberOfAccounts = Account.count()
    }

    def 'Create new account with valid parameters'() {
        setup:
        def account = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password: 'Test1234', name: 'Kelly Schrader')

        when:
        account.save(failOnError: true)

        then:
        !account.hasErrors()
        account.id
        Account.count == startingNumberOfAccounts + 1
        Account.get(account.id).email == 'schra435@umn.edu'
        Account.get(account.id).handle == 'Kelly'
        Account.get(account.id).password == 'Test1234'
        Account.get(account.id).name == 'Kelly Schrader'
    }

    def 'verify password and required fields: #description'() {
        given:
        def account = new Account(handle: handle, email: email, password: password, name: name)

        when:
        account.save()

        then:
        account.hasErrors()
        !account.id
        Account.count == startingNumberOfAccounts

        where:
        description                       |  handle    |   email                        |   password                |   name
        'No handle'                       |  ''        |   'kschrade@hotmail.com'       |   'Test12345'             |   'Kelly Schrader'
        'No email'                        |  'Kelly3'  |   ''                           |   'Test12345'             |   'Kelly Schrader'
        'No password'                     |  'Kelly4'  |   'kelly.j.schrader@gmail.com' |   ''                      |   'Kelly Schrader'
        'No name'                         |  'Kelly5'  |   'kelly.schrader@pearson.com' |   'Test12345'             |   ''
        'No capital letter in password'   |  'Kelly7'  |   'schra436@umn.edu'           |   'test12345'             |   'Kelly Schrader'
        'No lowercase letter in password' |  'Kelly8'  |   'schra437@umn.edu'           |   'TEST12345'             |   'Kelly Schrader'
        'No number in password'           |  'Kelly9'  |   'schra438@umn.edu'           |   'TestMeNow'             |   'Kelly Schrader'
        'Password length < 8'             |  'Kelly10' |   'schra439@umn.edu'           |   'TestMe'                |   'Kelly Schrader'
        'Password length > 16'            |  'Kelly11' |   'schra440@umn.edu'           |   'TestMeNow123456789'    |   'Kelly Schrader'
        'Invalid email'                   |  'Kelly12' |   'schra435'                   |   'Test12345'             |   'Kelly Schrader'

    }

    def 'testing creating valid follower'() {
        given:
        def masterAccount = new Account(handle: 'Kelly', email: 'schra435@umn.edu', password: 'Test1234', name: 'Kelly Schrader')
        def followingAccount = new Account(handle: 'KellysFollower', email: 'kschrade@yahoo.com', password: 'Test1234', name: 'Kelly Follower',following: [masterAccount])

        when:
        masterAccount.save(failOnError: true)
        followingAccount.save(failOnError: true)
        masterAccount.addToFollower(followingAccount)
        masterAccount.save(failOnError: true)

        then:
        masterAccount.errors.errorCount == 0
        masterAccount.id
        masterAccount.get(masterAccount.id).email == 'schra435@umn.edu'
        masterAccount.get(masterAccount.id).handle == 'Kelly'
        masterAccount.get(masterAccount.id).password == 'Test1234'
        masterAccount.get(masterAccount.id).name == 'Kelly Schrader'

        followingAccount.errors.errorCount == 0
        followingAccount.id
        followingAccount.get(followingAccount.id).email == 'kschrade@yahoo.com'
        followingAccount.get(followingAccount.id).handle == 'KellysFollower'
        followingAccount.get(followingAccount.id).password == 'Test1234'
        followingAccount.get(followingAccount.id).name == 'Kelly Follower'
    }
}
