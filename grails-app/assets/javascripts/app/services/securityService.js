angular.module('app').factory('securityService', ['$http', '$rootScope', 'webStorage', function ($http, $rootScope, webStorage) {
  var service = {};

  var currentUser;

  var setCurrentUser = function(user) {
    currentUser = user;
    webStorage.set('accountUser', currentUser);
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

  service.currentUser = function () {
    return currentUser;
  };

  setCurrentUser(webStorage.get('accountUser'));

  return service;
}]);