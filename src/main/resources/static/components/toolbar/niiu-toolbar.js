(() => {

  const template = `
    <md-toolbar class="md-transparent" md-elevation="0">
      <md-button class="md-icon-button"
                 @click="$emit('chat-clicked')">
        <md-icon class="ni-icon-primary">chat</md-icon>
      </md-button>
      <md-button class="md-icon-button"
                 @click="$emit('games-library-clicked')">
        <md-icon class="ni-icon-primary">sports_esports</md-icon>
      </md-button>
      <md-button class="md-icon-button"
                 @click="$emit('settings-clicked')">
        <md-icon class="ni-icon-primary">settings</md-icon>
      </md-button>
      <md-button class="md-icon-button"
                 @click="$emit('logout-clicked')">
        <md-icon class="ni-icon-danger">logout</md-icon>
      </md-button>
    </md-toolbar>
  `;

  Vue.component('niiu-toolbar', {
    props: [],
    template: template,
    data: function () {
      return { }
    }
  });

})();
