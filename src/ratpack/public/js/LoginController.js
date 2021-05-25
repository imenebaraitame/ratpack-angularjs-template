app.controller('LoginController', function ($state, AuthenticationService) {

  var vm = this;

  vm.error = '';
  vm.username = '';
  vm.password = '';
  vm.loading = false;

  // Login a User
  vm.login = function(){
    console.log('logging...');
    vm.loading = true;
AuthenticationService.Login(vm.username, vm.password, function (result) {
    if (result === true) {
        $state.go('home');
    } else {
        vm.error = 'Username or password is incorrect';
        vm.loading = false;
    }
});
  };

  // Logout
  vm.logout = function(){
  };

});
