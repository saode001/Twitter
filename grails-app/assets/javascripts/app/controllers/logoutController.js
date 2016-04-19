angular.module('app').controller('logoutController', function($scope, $location, securityService) {

    $scope.logout = function() {
        $scope.error = 'You have successfully logged out!';
        $location.path('/login');
    };
});