var app = angular.module('app', ['ui.router', 'ngResource', 'ngCookies', 'ui.bootstrap']);

app.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state({
      name: 'home',
      url: '/',
      controller: 'HomeController',
      templateUrl: 'list.html'
    })
    .state({
      name: 'create',
      url: '/create',
      templateUrl: 'user.html',
      controller: 'UserController'
    })
    .state({
      name: 'edit',
      url: '/edit/{userId}',
      templateUrl: 'user.html',
      controller: 'UserController'
    })
    .state({
      name: 'about',
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
