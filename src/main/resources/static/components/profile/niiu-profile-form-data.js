(() => {

  const template = `
    <form v-on:submit.prevent="onUpdateProfile">
      <md-field :class="usernameClasses">
          <label>Username</label>
          <md-input v-model="$v.userData.username.$model"
                    maxlength="16"></md-input>
          <span class="md-error">{{usernameErrorText}}</span>
      </md-field>
      
      <md-field :class="emailClasses">
          <label>Email</label>
          <md-input v-model="$v.userData.email.$model"
                    type="email"></md-input>
          <span class="md-error">{{emailErrorText}}</span>
      </md-field>
      <md-button class="md-raised md-primary"
                 type="submit"
                 v-show="!updateProfileAnimating"
                 :disabled="profileUpdateDisabled">Update</md-button>
      <niiu-check-animation :show="updateProfileAnimating"></niiu-check-animation>
    </form>
  `;

  Vue.component('niiu-profile-form-data', {
    props: ['user'],
    template: template,
    data: function () {
      return {
        userData: Vue.util.extend({}, this.user),
        updateProfileAnimating: false
      }
    },
    computed: {
      usernameClasses: function () {
        return !this.$v.userData.username.required || !this.$v.userData.username.minLength ? 'md-invalid' : '';
      },
      usernameErrorText: function () {
        if (!this.$v.userData.username.required) return 'Field is required';
        else if (!this.$v.userData.username.minLength) return `Name must have at least ${this.$v.userData.username.$params.minLength.min} letters`;
        else return '';
      },
      emailClasses: function () {
        return !this.$v.userData.email.required || !this.$v.userData.email.valid ? 'md-invalid' : '';
      },
      emailErrorText: function () {
        if (!this.$v.userData.email.required) return 'Field is required';
        else if (!this.$v.userData.email.valid) return 'Must be a valid email';
        else return '';
      },
      profileUpdateDisabled: function () {
        return this.isProfileDataInvalid();
      }
    },
    validations: {
      userData: {
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
    methods: {
      isProfileDataInvalid: function () {
        return this.$v.userData.username.$invalid ||
            this.$v.userData.email.$invalid;
      },
      onUpdateProfile: async function () {
        if (this.isProfileDataInvalid()) return;

        try {
          await UserApi.updateUser({
            username: this.userData.username,
            email: this.userData.email
          });

          this.profileUpdateAnimation();
        } catch (err) {
          console.log(err);
          alert('Update failed. Check server logs.');
        }
      },
      profileUpdateAnimation: function () {
        this.updateProfileAnimating = true;
        setTimeout(() => this.updateProfileAnimating = false, 1500);
      }
    },
    watch: {
      user: function(newVal, oldVal) {
        this.userData = Vue.util.extend({}, newVal);
      }
    }
  });

})();
