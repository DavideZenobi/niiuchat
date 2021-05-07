(() => {

  class ChatsApi {

    getChatsByUserId() {
      return axios.get('/api/chats/');
    }

  }

  window.ChatsApi = new ChatsApi();

})();