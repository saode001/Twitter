angular.module('app').controller('searchController', function ($resource, $scope, $location, securityService, alertService) {

    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentUser.username});
    $scope.curAccount = Account.get();

    $scope.alerts = alertService.getAlerts();

    if(!$scope.alerts){
        $scope.alerts = [];
    }

    $scope.searchMessages=function(searchTerm){
        if(searchTerm.length){
            var Messages = $resource('/api/messages/search/:searchTerm', {searchTerm: searchTerm});

            var msg = Messages.query();

            msg.$promise.then(function(response){
                console.log(response);
                $scope.messages = response;
            });
        }
    };

    $scope.addNewMessage=function(){
        $location.path("/message")
    };

    $scope.closeAlert=function(index){
      alertService.closeAlert(index);
    };
});
