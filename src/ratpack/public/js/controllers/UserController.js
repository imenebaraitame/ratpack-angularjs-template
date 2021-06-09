app.controller('UserController', function ($scope, $stateParams, $state, User) {

  $scope.error = '';

  if ($stateParams.userId){
    $scope.user = User.get({id: $stateParams.userId}, function (user) {
      return user;
    });
  }

  // Create a User
  $scope.createUser = function(){
    if ($scope.user.username != ''){
      new User({
        username: $scope.user.username,
        password: $scope.user.password,
        isActive: $scope.user.isActive
      }).$save().then(function (data) {
        if (data.id > 0){
          $state.go('home');
        }
      }, function (e) {
        $scope.error = e.data;
      });
    }
  };

  // Update a user
  $scope.updateUser = function () {
    $scope.user.$update()
    // $scope.user.$save()
    .then(function () {
      $state.go('home');
    });
  };

});
