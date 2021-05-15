(() => {

  const template = `
    <div>
      <md-field>
        <md-input v-model="withLabel" placeholder="A nice placeholder"></md-input>
      </md-field>
    </div>
  `;

  Vue.component('niiu-chat-input', {
    props: [],
    template: template,
    data: function () {
      return {

      }
    },
  });

})();
