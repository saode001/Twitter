angular.module('app').controller('userDetailController', function ($resource, $scope,securityService, $routeParams, $location) {
   // to get current loggedin user
    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentUser.username});
    $scope.curAccount = Account.get();



    /* U1 Requirement*/
   var AccountDetails = $resource('/api/accounts/:accountId', {accountId: $routeParams.accountId});
    $scope.userDetails = AccountDetails.get();
   console.log(AccountDetails);

    var UserMessages = $resource('/api/accounts/:accountId/messages', {accountId: $routeParams.accountId});
    var Umsg = UserMessages.query();
    Umsg.$promise.then(function(response){
      console.log(response);
        $scope.postings = response});

    //U2 Requirement
    var followOrNot = $resource('/api/accounts/:accountId/followers', {accountId: $routeParams.accountId});
    var followOrNotUmsg = followOrNot.query();
    followOrNotUmsg.$promise.then(function(response){
        console.log(response);
        $scope.followers = response;
    });

    // UI 3 and 4
    $scope.getFollowStatus = function(accountId,followersList) {
        $scope.error = 'Error in getting followers';
        console.log(accountId);
        console.log(followersList);
        var n = followersList.length;
        var i =0;
        var temp = 0;
        for (i; i<n; i++){
            if ( accountId == followersList[i].id){
                console.log("Already following");
                temp = 1;
                //$scope.alreadyfollowing = 1;
                alert("Already following")
            }
        }
        if (temp == 0){
            var toFollow = window.confirm("Not a follower. Click OK to start following");
            if (toFollow == true) {
                console.log("Trying to follow");
                console.log(accountId);
                console.log($routeParams.accountId);
                var result = $resource('/api/accounts/:accountId?/follow/:followId?',{
                    update: {
                        method: 'POST',
                        params: {accountId: $routeParams.accountId, followId: accountId},
                        //params: {accountId: accountId, followId: $routeParams.accountId},
                        headers: {'X-Auth-Token': currentUser.token}
                    }
                });
                console.log(result);
                alert("Now Following");
            }
        }
    };
});





