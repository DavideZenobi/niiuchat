(() => {

  const template = `
    <div>
      <md-dialog-confirm
        :md-active.sync="showDeleteDialog"
        :md-title="'Delete Group ' + groupIdToDelete"
        md-content="Are you sure you want to delete the selected group?"
        md-confirm-text="Delete"
        md-cancel-text="Keep"
        @md-cancel="onCancel"
        @md-confirm="onConfirm" />
      
      <md-list class="md-double-line">
        <md-subheader>Chats</md-subheader>
        
        <md-list-item>
          <md-button class="md-icon-button md-raised"
                     @click="$emit('create-clicked')">
            <md-icon class="ni-icon-primary">add</md-icon>
          </md-button>
          <span class="md-list-item-text">New conversation</span>
        </md-list-item>
        
        <md-list-item v-for="chat in chats"
                      @click="onChatSelect(chat.groupId)"
                      :style="getChatItemStyle(chat)">
          <md-avatar class="md-avatar-icon md-primary">
            <img :src="getUserAvatar(chat.userId)">
          </md-avatar>
            
          <div class="md-list-item-text">
            <span>{{chat.username}}</span>
            <p>{{chat.groupId}}</p>
          </div>
          
          <md-button class="md-icon-button md-list-action"
                     @click="deleteChat(chat.groupId)">
            <md-icon class="ni-icon-danger">delete</md-icon>
          </md-button>
        </md-list-item>
      </md-list>
    </div>
  `;

  Vue.component('niiu-chats', {
    props: ['selectedGroup'],
    template: template,
    data: function (){
      return {
        chats: [],
        groupIdToDelete: '',
        showDeleteDialog: false
      };
    },
    mounted: async function () {
      this.loadChats();

      // Reload chat when a new group is created
      PubSub.subscribe(NiiuEvents.GROUP_CREATED, (topic, msg) => {
        this.loadChats();
      });

      PubSub.subscribe(NiiuEvents.GROUP_DELETED, (topic, msg) => {
        for (const chat of this.chats) {
          if (chat.groupId === msg.groupId) {
            const index = this.chats.indexOf(chat);
            this.chats.splice(index, 1);
            break;
          }
        }
      });
    },
    methods: {
      loadChats: async function () {
        const response = await ChatsApi.getChatsByUserId();
        this.chats = response.data;
      },
      getUserAvatar: function (userId) {
        return `/api/users/${userId}/avatar`;
      },
      onChatSelect: function (groupId) {
        this.$emit('chat-selected', groupId);
      },
      getChatItemStyle: function (chat) {
        const style = {};

        if (chat.groupId === this.selectedGroup) {
          style.background = '#ccc';
        }

        return style;
      },
      deleteChat: function (groupId) {
        this.groupIdToDelete = groupId;
        this.showDeleteDialog = true;
      },
      onCancel: function () {
        this.groupIdToDelete = '';
        this.showDeleteDialog = false;
      },
      onConfirm: async function () {
        const response = await ChatsApi.deleteChat(this.groupIdToDelete);
      }
    }
  });

})();
