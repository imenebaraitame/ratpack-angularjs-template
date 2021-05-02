var app = angular.module('app', ['ui.router', 'ngResource', 'ngCookies']);

app.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state({
      name: 'Home',
      url: '/',
      template: '<h1>Home</h1>'
    })
    .state({
      name: 'Users',
      url: '/users',
      template: '<h1>Users</h1>'
    })
    .state({
      name: 'About',
      url: '/about',
      template: '<h1>About</h1>'
    });

  $urlRouterProvider.otherwise('/');

});

app.controller('HomeController', function ($scope, $resource) {
  $scope.username = 'Master';
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

  // Create a User
  $scope.createUser = function(){
    if ($scope.username != ''){
      var newUser = {
        'id': $scope.users.length,
        'username': $scope.username
      };
      $scope.users.push(newUser);
      $scope.username = '';
    }
  }

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
