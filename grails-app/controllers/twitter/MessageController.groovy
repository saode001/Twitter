package twitter

import grails.rest.RestfulController

class MessageController extends RestfulController<Message> {

    static responseFormats = ['json', 'xml']
    MessageController() {
        super(Message)
    }

    @Override
    protected Message queryForResource(Serializable id) {
        def accountId = params.accountId
        Message.where {
            id == id && account.id == accountId
        }.find()
    }

    @Override
    protected Message createResource(){
        if(params.accountId){
            def account = Account.get(params.accountId)
            if(account) {
                new Message(messageText:request.JSON.messageText,account:account)
            }
        }
    }
}
