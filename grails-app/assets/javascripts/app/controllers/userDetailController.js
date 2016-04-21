angular.module('app').controller('userDetailController', function ($http, $resource, $scope, securityService, $routeParams, $route) {
   // to get current loggedin user
    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentUser.username});
    //var curUser = Account.get();
    $scope.curAccount = Account.get();


    /* U1 Requirement*/
    // get details for the user being viewed
    var AccountDetails = $resource('/api/accounts/:accountId', {accountId: $routeParams.accountId});
    var accDet = AccountDetails.get();
    accDet.$promise.then(function(response) {
        console.log(response);
        $scope.userDetails = response;
    })



    // get message for user being viewed
    var UserMessages = $resource('/api/accounts/:accountId/messages', {accountId: $routeParams.accountId});
    var Umsg = UserMessages.query();
    Umsg.$promise.then(function(response){
      console.log(response);
        $scope.postings = response});

    //U2 & U3 Determine if the currently logged in user is following the displayed user
    $scope.alreadyfollowing = 0;
    var followOrNot = $resource('/api/accounts/:accountId/followers', {accountId: $routeParams.accountId});
    followOrNot.query().$promise.then(function(response){
        angular.forEach(response, function(result){
            if(result.handle == currentUser.username){
                $scope.alreadyfollowing = 1;
            }

        });
        $scope.followers = response;
    });

    // UI 2: Add a way for currently logged in user to follow the displayed user.
    $scope.getFollowStatus = function(followerId,followeeId) {
        $http.get('/api/accounts/' + followerId + '/follow/' + followeeId).then(function (resp){
            console.log('add follower', resp);
            if (resp.status == 200) {
                $scope.alreadyfollowing = 1;
                alert("Now Following");
                $route.reload();
            }
        },
        function(fail) {
            alert("Adding follower failed");
        });
    };
});





