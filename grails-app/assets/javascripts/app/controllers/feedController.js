angular.module('app').controller('feedController', function ($resource, $scope) {

  var Account = $resource('/api/accounts/:accountId/followers', {accountId: '@id'});

  $scope.accounts = Account.query();

});