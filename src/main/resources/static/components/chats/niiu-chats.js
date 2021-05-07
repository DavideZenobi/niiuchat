(() => {

  const template = `
    <md-list class="md-double-line">
    
      <md-subheader>Chats</md-subheader>
    
      <md-list-item>
        <md-icon>add</md-icon>
        <span class="md-list-item-text">Create chat</span>
      </md-list-item>
      
        <md-list-item v-for="chat in chats">
          <md-avatar class="md-avatar-icon md-primary">
            <md-icon>home</md-icon>
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
      const response = await ChatApi.getChatsByUserId();
      this.chats = response.data;
    }
  });

})();