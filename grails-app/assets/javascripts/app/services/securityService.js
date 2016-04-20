angular.module('app').factory('securityService', ['$http', '$rootScope', '$location', function ($http, $rootScope, $location) {
  var loggedOut = false;
  var service = {};

  var currentUser;

  var setCurrentUser = function(user) {
    currentUser = user;
    $rootScope.$emit('userChange', currentUser);
  };

  var loginSuccess = function (response) {
    setCurrentUser({
      username: response.data.username,
      roles: response.data.roles,
      token: response.data['access_token']
    });
  };

  var loginFailure = function () {
    setCurrentUser(undefined);
  };

  service.login = function (username, password) {
    var loginPayload = {username: username, password: password};
    return $http.post('/api/login', loginPayload).then(loginSuccess, loginFailure);
  };

  service.logout = function (){
    currentUser = undefined;
    delete $rootScope.currentUser;
    loggedOut = true;
    $location.path('#/login?logout=1');
  };

  service.currentUser = function () {
    return currentUser;
  };

  service.loggedOut = function() {
    return loggedOut;
  };

  return service;
}]);