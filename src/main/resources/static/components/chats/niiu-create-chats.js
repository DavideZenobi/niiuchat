(() => {

  const template = `
    <md-list class="create-chats-container">
      <md-list-item v-for="user in users" class="create-chats-chat">
        <md-avatar class="md-large">
          <img :src="getUserAvatar(user.id)">
        </md-avatar>
        <div class="md-list-item-text">
          <span class="create-item">{{user.username}}</span>
        </div>
        <md-button class="create-chats-button" 
                   @click="createChat(user.id)">
          <span>TALK</span>
        </md-button>
      </md-list-item>
    </md-list>
  `;


  Vue.component('niiu-create-chats', {
    props: [],
    template: template,
    data: function () {
      return {
        users: { }
      }
    },
    mounted: async function () {
      const response = await UserApi.getAll();
      this.users = response.data;
    },
    methods: {
      getUserAvatar: function (userId) {
        return `/api/users/${userId}/avatar`;
      },
      createChat: async function (userId) {
        const response = await ChatsApi.createChat({
          userIds: [userId]
        });

        if (response.status === 200) {
          this.$emit('chat-clicked', response.data.groupId);
          PubSub.publish(NiiuEvents.GROUP_CREATED, response.data.groupId);
        }

      }
    }
  });

})();