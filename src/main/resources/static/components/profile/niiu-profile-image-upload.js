(() => {

  const template = `
    <div style="display: inline-block;">
      <md-card style="display: inline-block;">
        <md-card-media>
          <md-card-media>
            <img :src="profileImageSrc" alt="avatar" style="max-height: 18vh; width: auto;">
          </md-card-media>
          <md-card-actions>
            <md-button class="md-icon-button" @click="$refs.avatarInput.click()">
              <md-icon>upload</md-icon>
            </md-button>
          </md-card-actions>
        </md-card-media>
      </md-card>
      <input ref="avatarInput"
             type="file"
             accept="image/*"
             v-show="false"
             @change="(event) => $emit('image-selected', event.target.files)" />
    </div>
  `;

  Vue.component('niiu-profile-image-upload', {
    props: ['image'],
    template: template,
    data: function () {
      return {
        profileImage: '/images/default_avatar.png'
      }
    },
    computed: {
      profileImageSrc: function () {
        return this.image || this.profileImage;
      }
    }
  });

})();
