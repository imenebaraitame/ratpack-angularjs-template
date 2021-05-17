app.controller('UserController', function ($scope, $stateParams, $state, User) {

  if ($stateParams.userId){
    $scope.user = User.get({id: $stateParams.userId}, function (user) {
      return user;
    });
  }

  // Create a User
  $scope.createUser = function(){
    if ($scope.user.username != ''){
      var user = new User();
      user.username = $scope.user.username;
      user.password = $scope.user.password;
      user.$save().then(function () {
        $state.go('home');
      });
    }
  }

  $scope.updateUser = function () {
    $scope.user.$update()
    // $scope.user.$save()
    .then(function () {
      $state.go('home');
    });

  }

});
