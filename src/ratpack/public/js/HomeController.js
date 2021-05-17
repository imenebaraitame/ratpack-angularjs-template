app.controller('HomeController', function ($scope, $state, User) {

  // Get all users
  User.query(function (users) {
    $scope.users = users || [];
    if ($scope.users.length == 0){
      $state.transitionTo('create');
    }
  });

  // Delete a User
  $scope.deleteUser = function(user){
      User.delete({id: user.id}, function () {
        for (var i = 0; i < $scope.users.length; i++) {
          if ($scope.users[i].id === user.id){
            $scope.users.splice(i, 1);
          }
        }
      });
  }

});
