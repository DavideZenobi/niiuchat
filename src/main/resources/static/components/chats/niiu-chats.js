(() => {

  const template = `
    <md-field>
        
    </md-field>
  `;

  Vue.component('niiu-chats', {
    props: [],
    template: template,
    data: function (){
      return {
        chat: {
          username: '',
          groupId: '',
          avatar: ''
        }
      };
    },
    mounted: async function () {

    }
  });

})();