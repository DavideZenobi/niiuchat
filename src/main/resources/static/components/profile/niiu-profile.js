(() => {

  const template = `
    <div class="np-container">
      <h2>User data</h2>
      <form action="/update" method="post">
        <md-field>
            <label>Username</label>
            <md-input class="initial"></md-input>
        </md-field>
        <md-field>
            <label>Email</label>
            <md-input></md-input>
        </md-field>
        <md-field>
            <label>Password</label>
            <md-input type="password"></md-input>
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
      };
    },
    mounted: async function () {
      data.user = await UserApi.getCurrentUser();
    }
  });

})();
