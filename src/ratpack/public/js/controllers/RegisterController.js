app.controller('RegisterController', function ($scope, $state, $location, AuthenticationService) {

  $scope.vm = {};
  var vm = $scope.vm;

  vm.error = '';
  vm.username = '';
  vm.password = '';
  vm.repeatPassword = '';
  vm.loading = false;
  vm.agree = false;


  // Register
  vm.register = function(){
    vm.loading = true;
    AuthenticationService.Register(vm.username, vm.password, vm.agree, function (result) {
      if (result === true) {
          $location.path('/login');
      } else {
          vm.error = result;
          vm.loading = false;
      }
    });
  };

});
