package twtr

class Message {
    String messageText

    static belongsTo = [account: Account]

    static constraints = {
        messageText nullable: false, maxSize: 40
    }
}
