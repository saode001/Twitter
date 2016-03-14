package twitter

import grails.rest.RestfulController
import grails.converters.JSON

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
    protected Message createResource() {
        if (params.accountId) {
            def account = Account.get(params.accountId)
            if (account) {
                new Message(messageText: request.JSON.messageText, account: account)
            }
        }
    }


    @Override
    def show() {
        def msg = Message.findByAcc(parmas.accountId)
        def query = msg.list(max: 10, offset: 0 , sort: "Id", order: "desc")
        if (msg) {
            render query as JSON
        } else {
            response.status = 404
        }
    }

}