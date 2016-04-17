import twitter.Account
import twitter.Message
import twitter.Role
import twitter.UserRole

class BootStrap {

    def init = { servletContext ->
        def admin = new Account(handle: 'Pres1', email: 'George.Washington@whitehouse.gov', password: 'Password1', name: 'George Washington').save(flush: true, failOnError: true)
        def role = new Role(authority: 'ROLE_READ').save(flush: true, failOnError: true)
        new UserRole(user: admin, role: role).save(flush: true, failOnError: true)

        def Pres2 = new Account(handle: 'Pres2', email: 'John.Adams@whitehouse.gov', password: 'Password1', name: 'John Adams', following: admin)
        role = new Role(authority: 'ROLE_READ').save(flush: true, failOnError: true)
        new UserRole(user: Pres2, role: role).save(flush: true, failOnError: true)

        admin.addToFollower(Pres2)
        admin.save(flush: true, failOnError: true)

        def Pres3 = new Account(handle: 'Pres3', email: 'Thomas.Jefferson@whitehouse.gov', password: 'Password1', name: 'Thomas Jefferson', following: admin)
        role = new Role(authority: 'ROLE_READ').save(flush: true, failOnError: true)
        new UserRole(user: Pres3, role: role).save(flush: true, failOnError: true)

        admin.addToFollower(Pres3)
        admin.save(flush: true, failOnError: true)

        Pres3.addToFollower(admin)
        Pres3.save(flush: true, failOnError: true)
        admin.addToFollowing(Pres3)
        admin.save(flush: true, failOnError:true)

        def msg = new Message(messageText: 'I am John Adams',account:Pres2).save(flush: true,failOnError: true)
        msg = new Message(messageText: 'Call me Johnny',account:Pres2).save(flush: true,failOnError: true)
        msg = new Message(messageText: 'I respond to John, Johnny, or Jojo',account:Pres2).save(flush: true,failOnError: true)
        msg = new Message(messageText: 'Hello Everyone!',account:Pres2).save(flush: true,failOnError: true)
        msg = new Message(messageText: 'I am Thomas Jefferson',account:Pres3).save(flush: true,failOnError: true)
        msg = new Message(messageText: 'Do not call me Tommy',account:Pres3).save(flush: true,failOnError: true)





    }
    def destroy = {
    }
}
