app.controller('HomeController', function ($scope, $state, User, $modal) {

  // Get all users
  User.query(function (users) {
    $scope.users = users || [];
    if ($scope.users.length == 0){
      $state.transitionTo('create');
    }
  });

  // Delete a User
  $scope.deleteUser = function(user){

    $scope.deleteResult = false;

    var modalInstance = $modal.open({
      templateUrl: 'modal-delete',
      scope: $scope,
      resolve: {
          data: function() {
            return $scope.data;
          }
      }
    });
    $scope.yes = function () {
      $scope.deleteResult = true;
      modalInstance.close($scope.deleteResult);
    }
    $scope.cancel = function () {
      $scope.deleteResult = false;
      modalInstance.dismiss();
    }

    modalInstance.result.then(function (data) {
      if (data == true) {
        User.delete({id: user.id}, function () {
          for (var i = 0; i < $scope.users.length; i++) {
            if ($scope.users[i].id === user.id){
              $scope.users.splice(i, 1);
            }
          }
      });
    }
    });


  }

});
