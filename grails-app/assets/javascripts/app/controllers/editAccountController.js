/**
 * Created by Priyanka on 4/19/2016.
 */
angular.module('app').controller('editAccountController', function ($http, $resource, $scope, securityService, $location) {

    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentUser.username});
    $scope.editAccount = Account.get();


    $scope.edit = function() {
        $scope.error = 'Error editing the user records';
        $location.path('/editAccount');
    };

    $scope.save = function(updateName, updateEmail) {
        var result = $resource('api/accounts/:accountId', {accountId: $scope.editAccount.id},{
            update: {
                method: 'PUT',
                params: {name: updateName, email: updateEmail},
                headers: {'X-Auth-Token': currentUser.token}
            }
        });
        result.update();
        alert("Name and email updated!");
        $location.path('/userDetail/'+$scope.editAccount.id);
    };

});
