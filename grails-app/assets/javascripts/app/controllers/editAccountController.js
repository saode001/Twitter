/**
 * Created by Priyanka on 4/19/2016.
 */
angular.module('app').controller('editAccountController', function ($resource, $scope, securityService, $location) {

    var currentUser = securityService.currentUser();
    var Account = $resource('/api/accounts/:handle',{handle: currentUser.username});
    $scope.editAccount = Account.get();


    $scope.edit = function() {
        $scope.error = 'Error editing the user records';
        $location.path('/editAccount');
    };

    $scope.save = function(updateEmail) {
        $scope.error = 'Error updating the email address';
        console.log(updateEmail);
        var result = $resource('api/accounts/:accountId?', {email: 'updateEmail'},{
            update: {
                method: 'PUT',
                params: {accountId: $scope.editAccount.id, name: $scope.editAccount.name, email: updateEmail},
                headers: {'X-Auth-Token': currentUser.token}
            }
        });
        result.update();
        alert("Email Updated");
        location.reload();
    };

});
