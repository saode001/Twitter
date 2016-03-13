package twitter

import grails.rest.RestfulController

class MessageController extends RestfulController<Message> {

    static responseFormats = ['json', 'xml']
    MessageController() {
        super(Message)
    }

    @Override
    protected Message queryForResource(Serializable id) {
        def accountId = params.account
        Message.where {
            id == id && account.id == accountId
        }.find()
    }


}
