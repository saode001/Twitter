describe('messageController', function () {
    beforeEach(module('app'));


    var $controller;
    var $httpBackend;
    var $scope;
    var securityService = {
        currentUser: function() {
            return {username:"Pres1",roles:['admin'],'access_token':'abc123'};
        }
    };

    beforeEach(inject(function(_$controller_, _$httpBackend_,_securityService_) {
        $controller = _$controller_;
        $httpBackend = _$httpBackend_;

        $scope = {};
    }));

    describe('messageController', function() {
        beforeEach(function () {
           $controller('messageController',{$scope:$scope,securityService:securityService});
        });

        it('has a logged in user',function() {
            expect($scope.curAccount).toBeDefined();
        });

        it('posts a message',function() {
            $httpBackend.expectPOST('/api/accounts/1/messages',{messageText:'This is a new message'}).respond(200,{messageText: 'This is a new message',account:'1'});

            expect($scope.createMessage).toBeDefined();
        });

        it('errors when message is too long',function() {
            $httpBackend.expectPOST('/api/accounts/1/messages',{messageText:'This is a long message.  It is over forty characters'}).respond(400,{error: 400});
        })
    });
});