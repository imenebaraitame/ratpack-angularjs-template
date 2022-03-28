var app = angular
  .module('app', ['ui.router', 'ngResource', 'ngCookies', 'ui.bootstrap', 'ngStorage'])
  .run(run);

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
      name: 'login',
      url: '/login',
      templateUrl: 'login.html',
      controller: 'LoginController'
    })
    .state({
      name: 'register',
      url: '/register',
      templateUrl: 'register.html',
      controller: 'RegisterController'
    })
    .state({
      name: 'logout',
      url: '/logout',
      controller: 'LoginController'
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

function run($rootScope, $http, $location, $localStorage, $state) {
    // keep user logged in after page refresh
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    // This makes it easy to check if user logged in on the home page
    $rootScope.getCurrentUser = function () {
      return $localStorage.currentUser;
    }

    // redirect to login page if not logged in and trying to access a restricted page
    $rootScope.$on('$locationChangeStart', function (event, next, current) {
        var publicPages = ['/login', '/register', '/about'];
        var restrictedPage = publicPages.indexOf($location.path()) === -1;
        if (restrictedPage && !$localStorage.currentUser) {
          $state.go('login');
        }
    });
}
