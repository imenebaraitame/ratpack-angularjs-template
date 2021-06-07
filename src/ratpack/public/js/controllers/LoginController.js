app.controller('LoginController', function ($rootScope, $scope, $state, $location, $localStorage, AuthenticationService) {

  $scope.vm = {};
  var vm = $scope.vm;

  vm.error = '';
  vm.username = '';
  vm.password = '';
  vm.loading = false;
  vm.agree = false;

  // reset login status
  AuthenticationService.Logout();

  // Login a User
  vm.login = function(){
    vm.loading = true;
    AuthenticationService.Login(vm.username, vm.password, function (result) {
      console.log('result: ', result);
      if (result === true) {
          $rootScope.currentUser = $localStorage.currentUser;
          $location.path('/');
      } else {
          vm.error = result; //'Username or password is incorrect';
          vm.loading = false;
      }
    });
  };

});
