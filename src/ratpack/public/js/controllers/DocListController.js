app.controller('DocListController', function ($scope, $state, $http) {
          $http.get('/docList')
              .then(function successCallback(response) {
                alert("Succesfully connected to the LogicalDoc");
                $scope.directories = response.data;

              }, function errorCallback(response) {
                alert("Error connecting to LogicalDoc");
              });
});