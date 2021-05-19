(() => {

  class ChatsApi {

    getChatsByUserId() {
      return axios.get('/api/messaging/chats');
    }

    createChat(data) {
      return axios.post('/api/messaging/chats', data);
    }

    getMessagesByGroupId(data) {
      return axios.get('/api/messaging/messages', data);
    }

    sendMessage(data) {
      return axios.post('/api/messaging/message', data);
    }

  }

  window.ChatsApi = new ChatsApi();

})();