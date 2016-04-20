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

    //U2
   // pri Starts
    var followOrNot = $resource('/api/accounts/:accountId/followers', {accountId: $routeParams.accountId});
    var followOrNotUmsg = followOrNot.query();
    followOrNotUmsg.$promise.then(function(response){
        console.log(response);
        $scope.followers = response;
    });

    // pri Ends
});





