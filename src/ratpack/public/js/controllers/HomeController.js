app.controller('HomeController', function ($rootScope, $scope, $state, User, $modal, AuthenticationService) {

  var init = function () {
    if ($rootScope.getCurrentUser()){
      $scope.users = $scope.getAllUsers();
    }
  }

  // Get all users
  $scope.getAllUsers = function () {
    return User.query(function (users) {
      return users || [];
    });
  }

  // Delete a User
  $scope.deleteUser = function(user){
    var modalInstance = $modal.open({
      templateUrl: 'modal-delete',
      scope: $scope
    });

    $scope.yes = function () {
        User.delete({id: user.id}, function () {
          for (var i = 0; i < $scope.users.length; i++) {
            if ($scope.users[i].id === user.id){
              $scope.users.splice(i, 1);
            }
          }
      });
      modalInstance.close();
    }
    $scope.cancel = function () {
      modalInstance.dismiss();
    }
  }

  init();

});
