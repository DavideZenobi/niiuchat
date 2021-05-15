(() => {

  class ChatsApi {

    getChatsByUserId() {
      return axios.get('/api/messaging/chats');
    }

    createChat(data) {
      return axios.post('/api/messaging/chats', data);
    }

    getMessagesByGroupId() {
      return axios.get('/api/messaging/messages');
    }

  }

  window.ChatsApi = new ChatsApi();

})();