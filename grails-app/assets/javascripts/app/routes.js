angular.module('app')

  // configure the routes
  .config(function ($routeProvider) {

    $routeProvider
      .when('/login', {
        templateUrl: '/app/login.html',
        controller: 'loginController'
      })
      .when('/userDetail/:accountId?', {
        templateUrl: '/app/userDetail.html',
        controller: 'userDetailController'
      })
        .when('/editAccount', {
            templateUrl: '/app/editAccount.html',
            controller: 'editAccountController'
        })
      .when('/search', {
        templateUrl: '/app/search.html',
        controller: 'searchController'
      })
      .when('/message',{
        templateUrl: '/app/message.html',
        controller: 'messageController'
      })
      .when('/logout',{
        templateUrl: '',
        controller: 'logoutController'
      })
      .otherwise({
        redirectTo: '/login'
      })
  })
    
  // Protect all routes other than login
  .run(function ($rootScope, $location, securityService) {
    $rootScope.$on('$routeChangeStart', function (event, next) {
      if (next.$$route && next.$$route.originalPath != '/login') {
        if (!securityService.currentUser()) {
          $location.path('/login');
        }
      }
    });
  });


