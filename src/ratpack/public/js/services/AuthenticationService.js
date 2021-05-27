app.factory('AuthenticationService', function ($rootScope, $location, $http, $localStorage) {
  var service = {};

  service.Login = function (username, password, callback) {
    $http.post('/api/login', { username: username, password: password })
        .success(function (response) {
            // login successful if there's a token in the response
            if (response.token) {
                // store username and token in local storage to keep user logged in between page refreshes
                $localStorage.currentUser = { username: username, token: response.token };

                // add jwt token to auth header for all requests made by the $http service
                $http.defaults.headers.common.Authorization = 'Bearer ' + response.token;

                // execute callback with true to indicate successful login
                callback(true);
            } else {
                // execute callback with false to indicate failed login
                callback(false);
            }
        }).error(function (message) {
          callback(message);
        });
  };

  service.Register = function (username, password, agree, callback) {
    $http.post('/api/register', { username: username, password: password, isActive: agree})
        .success(function (response) {
            // register successful if a true result in the response
            if (response.result) {
                // execute callback with true to indicate successful registeration
                callback(true);
            } else {
                // execute callback with false to indicate failed registeration
                callback(false);
            }
        }).error(function (message) {
          callback(message);
        });
  };

  service.Logout = function () {
    $http.post('/api/logout', {})
        .success(function (response) {
          // remove user from local storage and clear http auth header
          delete $localStorage.currentUser;
          $rootScope.currentUser = false;
          $http.defaults.headers.common.Authorization = '';
          $location.path('/login');
        });
  };

  return service;

});
