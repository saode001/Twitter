angular.module('app').controller('userDetailController', function ($resource, $scope, $routeParams) {

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
});





