(() => {

  const template = `
    <div class="main-area-container">
      <niiu-progress-bar :show="uploadingImage"></niiu-progress-bar>
      <div class="padded-view">
        <div style="width: 100%; text-align: center;">
          <niiu-profile-image-upload :image="profileImage"
                                     @image-selected="onImageChange"></niiu-profile-image-upload>
        </div>
          
        <div>
          <niiu-profile-form-data :user="user"></niiu-profile-form-data>
        </div>
        
        <div>
          <h2>Password</h2>
          <niiu-profile-form-password></niiu-profile-form-password>
        </div>
      </div>
      
      <md-snackbar md-position="center" :md-duration="3000" :md-active.sync="showImageUploadedAlert">
        <span>Avatar image updated correctly</span>
      </md-snackbar>
    </div>
  `;

  Vue.component('niiu-profile', {
    template: template,
    data: function () {
      return {
        user: { },
        uploadingImage: false,
        showImageUploadedAlert: false,
        profileImage: '/images/default_avatar.png'
      }
    },
    mounted: async function () {
      const response = await UserApi.getCurrentUser();
      this.user = response.data;
    },
    methods: {
      onImageChange: function(files) {
        this.uploadingImage = true;
        const fr = new FileReader();

        // Callback after the image has been read
        fr.onload = async () => {
          const formData = new FormData();
          formData.append('avatar', files[0]);

          try {
            await UserApi.updateAvatar(formData);

            this.profileImage = fr.result;
            this.showImageUploadedAlert = true;
          } catch (err) {
            alert(err);
          } finally {
            this.uploadingImage = false;
          }
        };

        fr.readAsDataURL(files[0]);
      }
    }
  });

})();
