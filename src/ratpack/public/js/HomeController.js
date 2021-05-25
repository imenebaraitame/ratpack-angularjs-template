app.controller('HomeController', function ($scope, $state, User, $modal, AuthenticationService) {


  // reset login status
  AuthenticationService.Logout();

  // Get all users
  User.query(function (users) {
    $scope.users = users || [];
    // if ($scope.users.length == 0){
    //   $state.transitionTo('create');
    // }
  });

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

});
