angular.module('app').controller('searchController', function ($resource, $scope, $location) {

    $scope.searchMessages=function(searchTerm){
        if(searchTerm.length){
            delete $scope.error;
            var Messages = $resource('/api/messages/search/:searchTerm', {searchTerm: searchTerm});

            $scope.messages = Messages.query();
        }
        else{
            $scope.error = "No search term was entered.  Please enter a term and try again.";
        }
    }

    $scope.showDetails=function(accountId){
        $location.path('/details/');
    }
});
