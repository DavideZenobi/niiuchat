(() => {

  class ChatApi {

    getChatsByUserId() {
      return axios.get('/api/chats/');
    }

  }

  window.ChatApi = new ChatApi();

})();