app.controller('HomeController', function ($scope, User) {

  // Get all users
  $scope.users = User.query() || [];

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
