(() => {

  class ChatsApi {

    getChatsByUserId() {
      return axios.get('/api/chats/');
    }

  }

  window.ChatApi = new ChatsApi();

})();