app.factory('AuthenticationService', function ($rootScope, $location, $http, $localStorage) {
  var service = {};

  service.Login = function (username, password, callback) {
    $http.post('/api/login', { username: username, password: password })
        .then(function (response) {
            // login successful if there's a token in the response
            if (response.data.token) {
                // retrieve the JWT token
                const token = response.data.token;
              
                // store username and token in local storage to keep user logged in between page refreshes
                $localStorage.currentUser = { username: username, token: token };

                // add jwt token to auth header for all requests made by the $http service
                $http.defaults.headers.common.Authorization = 'Bearer ' + token;

                // execute callback with true to indicate successful login
                callback(true);
            } else {
                // execute callback with false to indicate failed login
                callback(false);
            }
        }, function (response) {
          // return back error message
          callback(response.data);
        });
  };

  service.Register = function (username, password, agree, callback) {
    $http.post('/api/register', { username: username, password: password, isActive: agree})
        .then(function (response) {
            // register successful if a true result in the response
            if (response.data.result) {
                // execute callback with true to indicate successful registeration
                callback(true);
            } else {
                // execute callback with false to indicate failed registeration
                callback(false);
            }
        }, function (error) {
          console.log('Error: ', error);
          // execute callback with false to indicate failed registeration
          callback(false);
        });
  };

  service.Logout = function () {
    $http.post('/api/logout', {})
        .then(function (response) {
          // remove user from local storage and clear http auth header
          delete $localStorage.currentUser;
          $http.defaults.headers.common.Authorization = '';
          $location.path('/login');
        }, function (error){
          console.log('Error: ', error);
        });
  };

  return service;

});
