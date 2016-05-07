angular.module('app').factory('alertService', ['$http', '$rootScope', '$location', function ($http, $rootScope, $location) {
    var alertSrv = {};

    var alerts = [];

    alertSrv.addAlert = function(type,message) {
        alerts.push({type: type, msg: message});
        $rootScope.$emit('alertChange', alerts);
    };

    alertSrv.closeAlert = function(index) {
        alerts.splice(index, 1);
    };

    alertSrv.getAlerts = function() {
      return alerts;
    };

    return alertSrv;
}]);
