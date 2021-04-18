(() => {

  class UserApi {

    register(data) {
      return axios.post('/public/api/users/register', data);
    }

    getAll() {
      return axios.get('/api/users/');
    }

    getCurrentUser() {
      return axios.get('/api/users/profile');
    }

  }

  window.UserApi = new UserApi();

})();