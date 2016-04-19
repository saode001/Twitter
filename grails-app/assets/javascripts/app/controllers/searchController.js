angular.module('app').controller('searchController', function ($resource, $scope, $location) {

    $scope.searchMessages=function(searchTerm){
        if(searchTerm.length){
            delete $scope.error;
            var Messages = $resource('/api/messages/search/:searchTerm', {searchTerm: searchTerm});

            var msg = Messages.query();

            msg.$promise.then(function(response){
                console.log(response);
                $scope.messages = response;
            });
        }
        else{
            $scope.error = "No search term was entered.  Please enter a term and try again.";
        }
    };
});
