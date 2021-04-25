(() => {

  const template = `
    <div class="main-area-container">
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
    </div>
  `;

  Vue.component('niiu-profile', {
    template: template,
    data: function () {
      return {
        user: { },
        profileImage: '/images/default_avatar.png'
      }
    },
    mounted: async function () {
      const response = await UserApi.getCurrentUser();
      this.user = response.data;
    },
    methods: {
      onImageChange: function(files) {
        const fr = new FileReader();
        fr.onload = () => {
          this.profileImage = fr.result;
        };
        fr.readAsDataURL(files[0]);

        console.log(files);
      }
    }
  });

})();
