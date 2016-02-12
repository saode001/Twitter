package twitter

class UserAccount {

    String name
    String password


    static constraints = {
        name(blank: false)
        password(blank: false)
    }
}
