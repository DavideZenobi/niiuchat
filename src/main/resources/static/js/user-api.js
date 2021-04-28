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

    updateUser(data) {
      return axios.post('/api/users/update/data', data);
    }

    updatePassword(data) {
      return axios.post('/api/users/update/password', data);
    }

    updateAvatar(formData) {
      return axios({
        method: 'post',
        url: '/api/users/update/avatar',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      });
    }

  }

  window.UserApi = new UserApi();

})();