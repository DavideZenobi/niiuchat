(() => {

  const template = `
    <md-list>
      <md-list-item>
        <p>HOLA</p>
      </md-list-item>
    </md-list>
    <niiu-chat-input></niiu-chat-input>
  `;

  Vue.component('niiu-chat', {
    props: [],
    template: template,
    data: function () {
      return {
        messages: { },
        user: { }
      }
    },
    mounted: async function () {
      const response = await ChatsApi.getMessagesByGroupId();
    }
  });

})();
