var app = angular.module('app', ['ui.router', 'ngResource', 'ngCookies']);

app.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state({
      name: 'Home',
      url: '/',
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

app.controller('HomeController', function ($scope, $resource) {
  $scope.user = {
    id: 0,
    username: '',
    password: ''
  };
  $scope.users = [];

  var User = $resource('/users');

  // Get all users
  User.query({}, function(users){
      users.forEach(function(user){
          $scope.users.push(user);
      });
  }, function(err){
      console.log('Error:', err);
  });

  // Delete a User
  $scope.deleteUser = function(user){
      var i = findById(user.id);
      $scope.users.splice(i, 1);
  }

  function findById(id){
      for (var index = 0; index < $scope.users.length; index++){
          if ($scope.users[index].id === id){
              return index;
          }
      }
  }

});
