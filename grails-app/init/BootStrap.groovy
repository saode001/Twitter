import twitter.Account
import twitter.Role
import twitter.UserRole

class BootStrap {

    def init = { servletContext ->
      /*  def admin = new Account(handle: 'secuity', email: 'priyanka.security@gmail.com', password: 'Security1testing', name: 'Priyanka Saodekar').save(flush: true, failOnError: true)
        def role = new Role(authority: 'ROLE_READ').save(flush: true, failOnError: true)
        new UserRole(user: admin, role: role).save(flush: true, failOnError: true)*/
    }
    def destroy = {
    }
}
