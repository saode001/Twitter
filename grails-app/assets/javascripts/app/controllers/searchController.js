angular.module('app').controller('searchController', function ($resource, $scope, securityService) {

    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentUser.username});
    $scope.curAccount = Account.get();

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
});
