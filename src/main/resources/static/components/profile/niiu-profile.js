(() => {

  const template = `
    <div class="main-area-container">
      <h2>User data</h2>
      <form v-on:submit.prevent="onUpdateProfile">
        <md-field>
            <label>Username</label>
            <md-input v-model="user.username" class="initial"></md-input>
        </md-field>
        <md-field>
            <label>Email</label>
            <md-input v-model="user.email"></md-input>
        </md-field>
        <md-button class="md-raised md-primary" type="submit">Update</md-button>
      </form>
      <br>
      <br>
      <h2>Password</h2>
      <form v-on:submit.prevent="onUpdatePassword">
        <!--<md-field>
            <label>Old password</label>
            <md-input v-model="passwords.oldPassword" 
                      type="password"></md-input>
        </md-field>-->
        <md-field>
            <label>New password</label>
            <md-input v-model="passwords.newPassword"
                      type="password"></md-input>
        </md-field>
        <md-field>
            <label>Repeat new password</label>
            <md-input v-model="passwords.repeatNewPassword"
                      type="password"></md-input>
        </md-field>
        <md-button class="md-raised md-accent" type="submit">Update</md-button>
      </form>
    </div>
  `;

  Vue.component('niiu-profile', {
    props: [],
    template: template,
    data: function () {
      return {
        user: {},
        passwords: {
          /*oldPassword: '',*/
          newPassword: '',
          repeatNewPassword: ''
        }
      }
    },
    mounted: async function () {
      const response = await UserApi.getCurrentUser();
      this.user = response.data;
    },
    methods: {
      onUpdateProfile: async function () {

        try {
          await UserApi.updateUser({
            username: this.user.username,
            email: this.user.email
          });
        } catch (err) {
          console.log(err);
          alert('Update failed. Check server logs.');
        }
      },

      onUpdatePassword: async function () {

        try {

          if (this.passwords.newPassword !== this.passwords.repeatNewPassword) {
            return;
          }

          await UserApi.updatePassword({
            password: this.passwords.newPassword
          });
        } catch (err) {
          console.log(err);
          alert('Update failed. Check server logs.');
        }
      }
    }
  });

})();
