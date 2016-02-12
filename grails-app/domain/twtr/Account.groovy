package twtr

class Account {
    String handle
    String email
    String password
    String name


    static constraints = {
        handle nullable: false, unique: true
        email nullable: false, email: true, unique: true
        password nullable: false, size: 8..16, matches: "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])"
        name nullable: false

    }
}
