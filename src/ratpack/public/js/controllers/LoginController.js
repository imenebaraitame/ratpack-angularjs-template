app.controller('LoginController', function ($scope, $state, AuthenticationService) {

  $scope.vm = {};
  var vm = $scope.vm;

  vm.error = '';
  vm.username = '';
  vm.password = '';
  vm.loading = false;

  // reset login status
  AuthenticationService.Logout();

  // Login a User
  vm.login = function(){
    vm.loading = true;
    AuthenticationService.Login(vm.username, vm.password, function (result) {
      if (result === true) {
          $state.go('home');
      } else {
          vm.error = result; //'Username or password is incorrect';
          vm.loading = false;
      }
    });
  };

});
