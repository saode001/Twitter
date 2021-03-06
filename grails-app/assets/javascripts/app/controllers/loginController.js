angular.module('app').controller('loginController', function($scope, $location, securityService) {
  
  $scope.loginAttempt = {};
  $scope.loggedOut = securityService.loggedOut();

  $scope.doLogin = function() {
    securityService
      .login($scope.loginAttempt.username, $scope.loginAttempt.password)
      .finally(function(result){
        var currentUser = securityService.currentUser();
        if (currentUser) {
          delete $scope.error;
          $location.path('/search');
        } else {
          $scope.error = 'Invalid login';
        }
      });
  };
  
});