app.factory('AuthenticationService', function ($http, $localStorage) {
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
        });
  };

  service.Logout = function () {
      // remove user from local storage and clear http auth header
      delete $localStorage.currentUser;
      $http.defaults.headers.common.Authorization = '';
  };

  return service;

});
