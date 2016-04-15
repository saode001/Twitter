package twitter

import javax.management.relation.Role

class Account {

    transient springSecurityService


    String handle
    String email
    String password
    String name
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false

    static hasMany = [messages: Message, follower: Account, following: Account]

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ?
                springSecurityService.encodePassword(password) :
                password
    }


    static constraints = {
        handle nullable: false, unique: true
        email nullable: false, email: true, unique: true
        password nullable: false, matches: /((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,16})/
        name nullable: false

    }
}
