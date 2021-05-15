(() => {

  const template = `
    <md-list class="md-double-line">
    
      <md-subheader>Chats</md-subheader>
      
      <md-list-item>
        <md-button class="md-icon-button md-raised"
                   @click="$emit('chats-clicked')">
          <md-icon class="ni-icon-primary">add</md-icon>
        </md-button>
        <span class="md-list-item-text">Search users</span>
      </md-list-item>
      
      <md-list-item v-for="chat in chats" @click="sendGroupId(chat.groupId)">
        <md-avatar class="md-avatar-icon md-primary">
          <img :src="getUserAvatar(chat.userId)">
        </md-avatar>
          
        <div class="md-list-item-text">
          <span>{{chat.username}}</span>
          <p>{{chat.groupId}}</p>
        </div>
        
        <md-button class="md-icon-button md-list-action">
          <md-icon class="md-primary">star_border</md-icon>
        </md-button>
        <md-button class="md-icon-button md-list-action">
          <md-icon class="ni-icon-danger">delete</md-icon>
        </md-button>
      </md-list-item>
      
    </md-list>
  `;

  Vue.component('niiu-chats', {
    props: [],
    template: template,
    data: function (){
      return {
        chats: { }
      };
    },
    mounted: async function () {
      const response = await ChatsApi.getChatsByUserId();
      this.chats = response.data;
    },
    methods: {
      getUserAvatar: function (userId) {
        return `/api/users/${userId}/avatar`;
      },
      sendGroupId: function (groupId) {
        this.$emit('chat-clicked', groupId);
      }
    }
  });

})();