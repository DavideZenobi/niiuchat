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
            <md-button class="md-raised md-primary"
                       type="submit"
                       v-show="!updateProfileAnimating"
                       :disabled="profileUpdateDisabled">Update</md-button>
            <niiu-check-animation :show="updateProfileAnimating"></niiu-check-animation>
          </form>
        </div>
        
        <div>
          <h2>Password</h2>
          <form v-on:submit.prevent="onUpdatePassword">
            <md-field :class="newPasswordClasses">
                <label>New password</label>
                <md-input v-model="$v.newPassword.$model"
                          type="password"
                          maxlength="16"></md-input>
                <span class="md-error">{{newPasswordErrorText}}</span>
            </md-field>
            <md-field :class="repeatNewPasswordClasses">
                <label>Repeat new password</label>
                <md-input v-model="$v.repeatNewPassword.$model"
                          type="password"
                          maxlength="16"></md-input>
                <span class="md-error">{{repeatNewPasswordErrorText}}</span>
            </md-field>
            <md-button class="md-raised md-accent" 
                       type="submit"
                       v-show="!updatePasswordProfileAnimating"
                       :disabled="passwordUpdateDisabled">Update</md-button>
            <niiu-check-animation :show="updatePasswordProfileAnimating"></niiu-check-animation>
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
      },
      profileUpdateDisabled: function () {
        return this.isProfileDataInvalid();
      },
      newPasswordClasses: function () {
        return (!this.$v.newPassword.required || !this.$v.newPassword.minLength) && this.$v.newPassword.$dirty ? 'md-invalid' : '';
      },
      newPasswordErrorText: function () {
        if (!this.$v.newPassword.required) return 'Field is required';
        else if (!this.$v.newPassword.minLength) return `Password must have at least ${this.$v.newPassword.$params.minLength.min} letters.`;
        else return '';
      },
      repeatNewPasswordClasses: function () {
        return (!this.$v.repeatNewPassword.required || !this.$v.repeatNewPassword.minLength) && this.$v.repeatNewPassword.$dirty ? 'md-invalid' : '';
      },
      repeatNewPasswordErrorText: function () {
        if (!this.$v.repeatNewPassword.required) return 'Field is required';
        else if (!this.$v.repeatNewPassword.minLength) return `Password must have at least ${this.$v.repeatNewPassword.$params.minLength.min} letters.`;
      },
      passwordUpdateDisabled: function () {
        return this.isProfilePasswordInvalid();
      }
    },
    data: function () {
      return {
        user: { },
        newPassword: '',
        repeatNewPassword: '',
        updateProfileAnimating: false,
        updatePasswordProfileAnimating: false
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
      },
      newPassword: {
        required: validators.required,
        minLength: validators.minLength(4)
      },
      repeatNewPassword: {
        required: validators.required,
        minLength: validators.minLength(4)
      }
    },
    mounted: async function () {
      const response = await UserApi.getCurrentUser();
      this.user = response.data;
    },
    methods: {
      isProfileDataInvalid: function () {
        return this.$v.user.username.$invalid ||
            this.$v.user.email.$invalid;
      },
      isProfilePasswordInvalid: function () {
        return this.$v.newPassword.$invalid ||
            this.$v.repeatNewPassword.$invalid ||
            this.newPassword !== this.repeatNewPassword;
      },
      onUpdateProfile: async function () {
        if (this.isProfileDataInvalid()) return;

        try {
          await UserApi.updateUser({
            username: this.user.username,
            email: this.user.email
          });

          this.profileUpdateAnimation();
        } catch (err) {
          console.log(err);
          alert('Update failed. Check server logs.');
        }
      },
      onUpdatePassword: async function () {
        if (this.isProfilePasswordInvalid()) return;

        try {
          await UserApi.updatePassword({
            password: this.newPassword
          });

          this.profilePasswordUpdateAnimation();
        } catch (err) {
          console.log(err);
          alert('Update failed. Check server logs.');
        }
      },
      profileUpdateAnimation: function () {
        this.updateProfileAnimating = true;
        setTimeout(() => this.updateProfileAnimating = false, 1500);
      },
      profilePasswordUpdateAnimation: function () {
        this.updatePasswordProfileAnimating = true;
        setTimeout(() => this.updatePasswordProfileAnimating = false, 1500);
      }
    }
  });

})();
