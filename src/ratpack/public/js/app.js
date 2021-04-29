var app = angular.module('app', ['ui.router', 'ngResource', 'ngCookies']);

app.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state({
      name: 'Home',
      url: '/',
      template: '<h1>Home</h1>'
    })
    .state({
      name: 'About',
      url: '/about',
      template: '<h1>About</h1>'
    });

  $urlRouterProvider.otherwise('/');

});

app.controller('HomeController', function ($scope) {
  $scope.username = 'Admin';
});
