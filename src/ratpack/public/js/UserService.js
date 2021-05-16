app.service('User', function ($resource) {
  return   $resource('/users/:id'/*, null, {
      'update': { method: 'PUT', params: {id: "@id"}}
    }*/);
});
