app.controller('UserController', function ($scope, $resource) {
  $scope.user = {
    id: 0,
    username: '',
    password: ''
  };
  
  var User = $resource('/users');

  // Create a User
  $scope.createUser = function(){
    if ($scope.user.username != ''){
      var newUser = {
        'username': $scope.user.username,
        'password': $scope.user.password
      };
      User.query({method: 'POST', 'user':newUser}, function (data) {
        console.log(data);
      });
      
      $scope.user = {
        id: 0,
        username: '',
        password: ''
      }
    }
  }

});
