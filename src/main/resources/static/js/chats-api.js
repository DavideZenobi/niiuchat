(() => {

  class ChatsApi {

    getChatsByUserId() {
      return axios.get('/api/messaging/chats');
    }

    createChat(data) {
      return axios.post('/api/messaging/chats', data);
    }

    getMessagesByGroupId(data) {
      return axios.get(`/api/messaging/messages/${data.groupId}/?limit=${data.limit}&offset=${data.offset}`);
    }

    sendMessage(formData) {
      return axios({
        method: 'post',
        url: '/api/messaging/messages',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      });
    }

    deleteChat(groupId) {
      return axios.delete(`/api/messaging/chats/${groupId}/`);
    }

  }

  window.ChatsApi = new ChatsApi();

})();
