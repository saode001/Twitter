// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.1.3.js
//= require ../bower/bootstrap.js
//= require ../bower/angular/angular.js
//= require ../bower/angular-resource/angular-resource.js
//= require ../bower/angular-route/angular-route.js
//= require ../bower/angular-webstorage/angular-webstorage.js
//= require_tree app
var app = angular.module('app', ['ngRoute']);
/*if (typeof jQuery !== 'undefined') {
    (function($) {
        $('#spinner').ajaxStart(function() {
            $(this).fadeIn();
        }).ajaxStop(function() {
            $(this).fadeOut();
        });
    })(jQuery);
}

// Create the angular application called 'app'

//angular.module('app', ['ngRoute', 'ngResource', 'webStorageModule']);

// Define a controller called 'welcomeController'
//angular.module('app').controller('LoginController', function($scope) {
   // $scope.greeting = 'Hello Stranger'
});
    */