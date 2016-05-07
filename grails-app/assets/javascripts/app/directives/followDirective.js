/**
 * Created by Priyanka on 5/5/2016.
 */
angular.module('app').directive('followDirective',function(){
    return{
        template:
        '<button id="startfollow" align="right" type="button" class="btn btn-info"' +
        'ng-show="curAccount.id != userDetails.id && alreadyfollowing">Following'+
        '</button>'+
        '<button id="areFollowing" align="right type="button" class="btn-btn-success"'+
        'ng-show="curAccount.id != userDetails.id && !alreadyfollowing"'+
        'ng-click="getFollowStatus(curAccount.id, userDetails.id)">Follow'+
        '</button>'


    };
});