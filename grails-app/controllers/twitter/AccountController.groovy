package twitter

import grails.converters.JSON
import grails.rest.RestfulController
import java.text.SimpleDateFormat

class AccountController extends RestfulController<Account> {

    static responseFormats = ['json', 'xml']

    AccountController() {
        super(Account)
    }

    def followAccount() {
        if (params.id) {
            if (params.followId) {
                if (params.id != params.followId) {
                    def followerAccount = Account.get(params.id)
                    if (followerAccount) {
                        def followingAccount = Account.get(params.followId)
                        if (followingAccount) {
                            followerAccount.following.add(followingAccount)
                            followingAccount.follower.add(followerAccount)
                            respond followerAccount
                        } else {
                            response.status = 422
                            respond error: 422, message: "followID account does not exist"
                        }
                    } else {
                        response.status = 422
                        respond error: 422, message: "id account does not exist"
                    }
                } else {
                    response.status = 422
                    respond error: 422, message: "Id and followID cannot match"
                }
            } else {
                response.status = 422
                respond error: 422, message: "followID missing"
            }
        } else {
            response.status = 422
            respond error: 422, message: "Id missing"
        }
    }

    def showFollowers() {
        if (params.id) {
            def max = 10
            def offset = 0
            def accountID = params.id
            def account = Account.get(accountID)
            if (account) {
                if (params.max) {
                    max = params.max
                }

                if (params.offset) {
                    offset = params.offset
                }
                def followers = Account.findAll('from Account acc where acc.id in (:accounts) order by acc.id desc',
                        [accounts: Account.get(accountID).follower.id],
                        [max: max, offset: offset])

                render followers as JSON
            } else {
                response.status = 422
                render error:422, message:"Specified account with account ID ${accountID} doesn't exists"
            }
        }
        else{
            response.status = 422
            render error:422, message:"Missing id"
        }
    }

    def returnFeed(){
        int maximum = params.max == null ? 10 : Integer.parseInt(params.max)
        int offset = params.offset == null ? 0 : Integer.parseInt(params.offset)
        def followAccounts = Account.get(params.id).follower.id

        def msgList = Message.createCriteria().list(max: maximum, offset: offset){
            'in(account, $followAccounts)'
            if (params.beforeDate != null) {
                gte('dateCreated', new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(params.beforeDate))
            }
            order('dateCreated','desc')
        }
        respond msgList
    }

    @Override
    def show() {
        if ((params.id as String).isNumber()) {
            def account = Account.get(params.id)
            if (account) {
                def accountJSON = JSON.parse((account as JSON).toString())
                accountJSON.put("numFollowers", account.follower.size())
                accountJSON.put("numFollowing", account.following.size())
                respond accountJSON
            } else {
                response.status = 422
                respond error: 422, message: "Account ID does not exist"
            }
        } else {
            def account = Account.findByHandle(params.id)
            if (account) {
                def accountJSON = JSON.parse((account as JSON).toString())
                accountJSON.put("numFollowers", account.follower.size())
                accountJSON.put("numFollowing", account.following.size())
                respond accountJSON
            } else {
                response.status = 422
                respond error: 422, message: "Account handle does not exist"
            }
        }
    }
}
