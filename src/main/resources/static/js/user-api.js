(() => {

  class UserApi {

    register(data) {
      return axios.post('/public/api/users/register', data);
    }

    getAll() {
      return axios.get('/public/api/users/');
    }

  }

  window.UserApi = new UserApi();

})();