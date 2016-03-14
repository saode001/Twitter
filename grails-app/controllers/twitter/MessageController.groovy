package twitter

import grails.rest.RestfulController
import grails.converters.JSON

class MessageController extends RestfulController<Message> {

    static responseFormats = ['json', 'xml']

    MessageController() {
        super(Message)
    }

   def index() {
       params.max = Math.min(max ?: 10, 100)
       def accountId = params.accountId
       if (accountId) {
           def accountInfo = (params.accountId as String).isNumber()
           if (accountInfo) {
               accountId = Account.get(params.accountId)
           } else {
               accountId = Account.findByAccountHandle(params.accountId)
           }
           def msgList = Message.findAllByAcc(accountId, [max: params.max, sort: "Id", order: "desc", offset: params.offset, text: params.text])
           respond msgList
       } else {
           respond(status: 404, msgError: "No message found")
       }
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
           def query = msg.list(max: 10, sort: "Id", order: "desc")
           if (msg) {
               render query as JSON
           } else {
               response.status = 404
           }
       }

   }