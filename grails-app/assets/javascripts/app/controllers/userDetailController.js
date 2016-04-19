angular.module('app').controller('userDetailController', function ($resource, $scope, $routeParams) {

    var AccountDetails = $resource('/api/accounts/:accountId', {accountId: $routeParams.accountId});

    $scope.userDetails = AccountDetails.get();
    console.log(AccountDetails);
});