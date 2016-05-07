angular.module('app').controller('messageController', function ($http, $resource, $scope, $location, securityService, alertService) {
    // to get current loggedin user
    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle', {handle: currentUser.username});
    //var curUser = Account.get();
    $scope.curAccount = Account.get();

    $scope.createMessage=function(messageText){
        if(messageText && messageText.length){
          $http.post('/api/accounts/' + $scope.curAccount.id + '/messages',{messageText: messageText}
            ).then(function(response){
                alertService.addAlert('success','Message Posted!');
                //$scope.alerts.push({type: 'success',msg:'Message Posted!'});
                $location.path('/userDetail/' + $scope.curAccount.id);
            },
            function(response) {
                alertService.addAlert('danger','Error Posting Message.');
                //$scope.alerts.push({type: 'danger',msg:'Error Posting Message.'});
            });
        }
    };
});
