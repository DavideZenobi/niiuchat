(() => {

  const template = `
    <div class="np-container">
      <h2>User data</h2>
      <form action="/update" method="post">
        <md-field>
            <label>Username</label>
            <md-input v-model="user.username" class="initial"></md-input>
        </md-field>
        <md-field>
            <label>Email</label>
            <md-input v-model="user.email"></md-input>
        </md-field>
        <md-field>
            <label>Password</label>
            <md-input v-model="user.password" type="password"></md-input>
        </md-field>
        <md-field>
            <label>New Password</label>
            <md-input type="password"></md-input>
        </md-field>
        <md-field>
            <label>Repeat Password</label>
            <md-input type="password"></md-input>
        </md-field>
      </form>
    </div>
  `;

  Vue.component('niiu-profile', {
    props: [],
    template: template,
    data: function () {
      return {
        user: {}
      }
    },
    mounted: async function () {
      const response = await UserApi.getCurrentUser();
      this.user = response.data;
    }
  });

})();
