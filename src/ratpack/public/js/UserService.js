app.service('User', function ($resource) {
  return $resource('/users/:id', {id: "@id"}, {
      'update': { method: 'PUT'}
    });
});
