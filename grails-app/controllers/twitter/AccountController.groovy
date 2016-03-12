package twitter

import grails.rest.RestfulController

class AccountController extends RestfulController<Account> {

    static responseFormats = ['json', 'xml']
    AccountController() {
        super(Account)
    }

    @Override
    def show(){
        if ((params.id as String).isNumber()) {
            respond Account.get(params.id)
        }
        else {
            respond Account.findByHandle(params.id)
        }
    }
}
