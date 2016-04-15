angular.module('app').controller('feedController', function ($resource, $scope) {

  var Restaurant = $resource('/api/restaurants/:restaurantId', {restaurantId: '@id'});

  $scope.restaurants = Restaurant.query();
  
});