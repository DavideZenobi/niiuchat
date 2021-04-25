(() => {

  const template = `
    <form v-on:submit.prevent="onUpdatePassword">
      <md-field :class="newPasswordClasses">
          <label>New password</label>
          <md-input v-model="$v.newPassword.$model"
                    type="password"
                    maxlength="20"></md-input>
          <span class="md-error">{{newPasswordErrorText}}</span>
      </md-field>
      <md-field :class="repeatNewPasswordClasses">
          <label>Repeat new password</label>
          <md-input v-model="$v.repeatNewPassword.$model"
                    type="password"
                    maxlength="20"></md-input>
          <span class="md-error">{{repeatNewPasswordErrorText}}</span>
      </md-field>
      <md-button class="md-raised md-accent" 
                 type="submit"
                 v-show="!updatePasswordProfileAnimating"
                 :disabled="passwordUpdateDisabled">Update</md-button>
      <niiu-check-animation :show="updatePasswordProfileAnimating"></niiu-check-animation>
    </form>
  `;

  Vue.component('niiu-profile-form-password', {
    props: [],
    template: template,
    data: function () {
      return {
        newPassword: '',
        repeatNewPassword: '',
        updatePasswordProfileAnimating: false
      }
    },
    computed: {
      newPasswordClasses: function () {
        return (!this.$v.newPassword.required || !this.$v.newPassword.minLength) && this.$v.newPassword.$dirty ? 'md-invalid' : '';
      },
      newPasswordErrorText: function () {
        if (!this.$v.newPassword.required) return 'Field is required';
        else if (!this.$v.newPassword.minLength) return `Password must have at least ${this.$v.newPassword.$params.minLength.min} letters`;
        else return '';
      },
      repeatNewPasswordClasses: function () {
        return (!this.$v.repeatNewPassword.required || !this.$v.repeatNewPassword.minLength || !this.$v.repeatNewPassword.passwordMatch) && this.$v.repeatNewPassword.$dirty ? 'md-invalid' : '';
      },
      repeatNewPasswordErrorText: function () {
        if (!this.$v.repeatNewPassword.required) return 'Field is required';
        else if (!this.$v.repeatNewPassword.minLength) return `Password must have at least ${this.$v.repeatNewPassword.$params.minLength.min} letters`;
        else if (!this.$v.repeatNewPassword.passwordMatch) return `Passwords don't match`;
      },
      passwordUpdateDisabled: function () {
        return this.isProfilePasswordInvalid();
      }
    },
    validations: {
      newPassword: {
        required: validators.required,
        minLength: validators.minLength(4)
      },
      repeatNewPassword: {
        required: validators.required,
        minLength: validators.minLength(4),
        passwordMatch: function (value) {
          return value === this.newPassword;
        }
      }
    },
    methods: {
      isProfilePasswordInvalid: function () {
        return this.$v.newPassword.$invalid ||
            this.$v.repeatNewPassword.$invalid;
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
      profilePasswordUpdateAnimation: function () {
        this.updatePasswordProfileAnimating = true;
        setTimeout(() => this.updatePasswordProfileAnimating = false, 1500);
      }
    }
  });

})();
