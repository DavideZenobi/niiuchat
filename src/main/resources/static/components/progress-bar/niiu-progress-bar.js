(() => {

  const template = `
    <md-progress-bar :style="progressBarStyle" md-mode="indeterminate"></md-progress-bar>
  `;

  Vue.component('niiu-progress-bar', {
    props: ['show'],
    template: template,
    data: function () {
      return { }
    },
    computed: {
      progressBarStyle: function () {
        return {
          visibility: this.show ? 'visible': 'hidden'
        };
      }
    }
  });

})();
