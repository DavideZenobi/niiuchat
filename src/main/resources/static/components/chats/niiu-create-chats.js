(() => {

  const template = `
    <md-list>
      <md-list-item v-for="user in users">
        <md-avatar>
          <img :src="getUserAvatar(user.id)">
        </md-avatar>
        <div class="md-list-item-text">
          <span>{{user.username}}</span>
        </div>
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
      console.log(response.data);
      this.users = response.data;
    },
    methods: {
      getUserAvatar: function (userId) {
        return `/api/users/${userId}/avatar`;
      }
    }
  });

})();