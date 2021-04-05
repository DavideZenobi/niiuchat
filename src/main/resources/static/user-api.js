(() => {

  class UserApi {

    register(data) {
      return axios.post('/public/api/users/register', data);
    }

  }

  window.UserApi = new UserApi();

})();