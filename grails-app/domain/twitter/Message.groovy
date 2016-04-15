package twitter

class Message {
    String messageText
    Date dateCreated

    static belongsTo = [account: Account]

    static constraints = {
        messageText nullable: false, maxSize: 40
    }
}
