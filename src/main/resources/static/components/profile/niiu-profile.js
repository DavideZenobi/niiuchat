(() => {

  const template = `
    <div class="main-area-container">
      <div class="padded-view">
        <div>
          <h2>User data</h2>
          <form v-on:submit.prevent="onUpdateProfile">
            <md-field :class="usernameClasses">
                <label>Username</label>
                <md-input v-model="$v.user.username.$model"
                          class="initial"></md-input>
                <span class="md-error">{{usernameErrorText}}</span>
            </md-field>
            
            <md-field :class="emailClasses">
                <label>Email</label>
                <md-input v-model="$v.user.email.$model"></md-input>
                <span class="md-error">{{emailErrorText}}</span>
            </md-field>
            <md-button class="md-raised md-primary" type="submit">Update</md-button>
          </form>
        </div>
        
        <div>
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
      </div>
    </div>
  `;

  Vue.component('niiu-profile', {
    props: [],
    template: template,
    computed: {
      usernameClasses: function () {
        return !this.$v.user.username.required || !this.$v.user.username.minLength ? 'md-invalid' : '';
      },
      usernameErrorText: function () {
        if (!this.$v.user.username.required) return 'Field is required';
        else if (!this.$v.user.username.minLength) return `Name must have at least ${this.$v.user.username.$params.minLength.min} letters.`;
        else return '';
      },
      emailClasses: function () {
        return !this.$v.user.email.required || !this.$v.user.email.valid ? 'md-invalid' : '';
      },
      emailErrorText: function () {
        if (!this.$v.user.email.required) return 'Field is required';
        else if (!this.$v.user.email.valid) return 'Must be a valid email';
        else return '';
      }
    },
    data: function () {
      return {
        user: { },
        passwords: {
          /*oldPassword: '',*/
          newPassword: '',
          repeatNewPassword: ''
        }
      }
    },
    validations: {
      user: {
        username: {
          required: validators.required,
          minLength: validators.minLength(4)
        },
        email: {
          required: validators.required,
          valid: validators.email
        }
      }
    },
    mounted: async function () {
      const response = await UserApi.getCurrentUser();
      this.user = response.data;
    },
    methods: {
      onUpdateProfile: async function () {
        if (this.$v.$invalid) return;

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
