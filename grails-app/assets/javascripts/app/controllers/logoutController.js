angular.module('app').controller('logoutController', function ($resource, $scope, $location, $http, $rootScope, securityService) {

    $scope.logout = function(){
        securityService.logout();
    };

});