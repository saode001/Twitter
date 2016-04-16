import twitter.Account
import twitter.Role
import twitter.UserRole

class BootStrap {

    def init = { servletContext ->
        def admin = new Account(handle: 'Pres1', email: 'George.Washington@whitehouse.gov', password: 'Password1', name: 'George Washington').save(flush: true, failOnError: true)
        def role = new Role(authority: 'ROLE_READ').save(flush: true, failOnError: true)
        new UserRole(user: admin, role: role).save(flush: true, failOnError: true)
    }
    def destroy = {
    }
}
