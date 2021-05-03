var app = angular.module('app', ['ui.router', 'ngResource', 'ngCookies']);

app.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state({
      name: 'Home',
      url: '/',
      controller: 'HomeController',
      templateUrl: 'list.html'
    })
    .state({
      name: 'Create a User',
      url: '/create',
      templateUrl: 'user.html',
      controller: 'UserController'
    })
    .state({
      name: 'About',
      url: '/about',
      template: '<h1>About</h1>'
    });

  $urlRouterProvider.otherwise('/');

});

/* Disable default behavior because by default, trailing slashes will be stripped from the calculated URLs
app.config(['$resourceProvider', function($resourceProvider) {
  // Don't strip trailing slashes from calculated URLs
  $resourceProvider.defaults.stripTrailingSlashes = false;
}]);*/

app.controller('HomeController', function ($scope, $resource) {
  $scope.users = [];

  var User = $resource('/users/:id');

  // Get all users
  $scope.users = User.query();

  // Delete a User
  $scope.deleteUser = function(user){
      User.delete({id: user.id}, function () {
        var index = $scope.users.filter(function (item) {
          return item.id !== user.id;
        });
        $scope.users.splice(index, 1);
      });
  }

});
