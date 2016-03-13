package twitter

import grails.converters.JSON
import grails.rest.RestfulController

class AccountController extends RestfulController<Account> {

    static responseFormats = ['json', 'xml']
    AccountController() {
        super(Account)
    }

   def followAccount() {
        if (params.id) {
            if(params.followId) {
                if(params.id != params.followId) {
                    def followerAccount = Account.get(params.id)
                    if (followerAccount) {
                        def followingAccount = Account.get(params.followId)
                        if (followingAccount) {
                            followerAccount.following.add(followingAccount)
                            followingAccount.follower.add(followerAccount)
                            respond followerAccount
                        }
                        else {
                            response.status = 422
                            respond error:422,message: "followID account does not exist"                        }
                    }
                    else {
                        response.status = 422
                        respond error:422,message: "id account does not exist"
                    }
                }
                else{
                    response.status = 422
                    respond error:422,message: "Id and followID cannot match"
                }
            }
            else{
                response.status = 422
                respond error:422, message: "followID missing"
            }
        }
       else{
            response.status = 422
            respond error:422, message: "Id missing"
        }
    }

   /* def showFollowers() {
        if(param.id) {
            def account = Account.get(param.id)
            def followers = account.findBy
            render followers
        }
    }*/

    @Override
    def show(){
        if ((params.id as String).isNumber()) {
            def account = Account.get(params.id)
            if(account){
                def accountJSON = JSON.parse((account as JSON).toString())
                accountJSON.put("numFollowers",account.follower.size())
                accountJSON.put("numFollowing",account.follower.size())
                respond accountJSON
            }
            else {
                response.status = 422
                respond error:422, message: "Account ID does not exist"
            }
        }
        else {
            def account = Account.findByHandle(params.id)
            if(account){
                def accountJSON = JSON.parse((account as JSON).toString())
                accountJSON.put("numFollowers",account.follower.size())
                accountJSON.put("numFollowing",account.follower.size())
                respond accountJSON
            }
            else {
                response.status = 422
                respond error:422, message: "Account handle does not exist"
            }
        }
    }
}
