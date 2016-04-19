angular.module('app').controller('userDetailController', function ($resource, $scope,securityService, $routeParams) {

    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentUser.username});
    $scope.curAccount = Account.get();



   var AccountDetails = $resource('/api/accounts/:accountId', {accountId: $routeParams.accountId});

    $scope.userDetails = AccountDetails.get();
    console.log(AccountDetails);

    var UserMessages = $resource('/api/accounts/:accountId/messages', {accountId: $routeParams.accountId});
    var Umsg = UserMessages.query();
    Umsg.$promise.then(function(response){
      console.log(response);
        $scope.postings = response});
   // $scope.userPostings = UserMessages.get();

   // var UserMessages = $resource('/api/accounts/:accountId/messages', {accountId: $routeParams.accountId});
   // $scope.postings = UserMessages.query();
   // console.log(postings);

    //U2 requirement
    var currentAuthToken = securityService.currentUser();
    var UserDetailQuery = $resource('/api/accounts/:query');
    //var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentAuthToken.username});
    $scope.curAccount = Account.get();

    $scope.followAccount=function(followAccount){
        if(followAccount.length){
         //   var Messages = $resource('/api/messages/search/:searchTerm', {searchTerm: searchTerm});
          var Account=  ("/api/accounts/$id/follow/$followId",{followAccount:followAccount});
            var acctn = Account.query();

            acctn.$promise.then(function(response){
                console.log(response);
                $scope.accounts = response;
            });
        }
    };


});





