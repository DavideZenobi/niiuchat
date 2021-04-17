(() => {

  const template = `
    <div class="main-area-container">
      <h2>Gamesss!</h2>
    </div>
  `;

  Vue.component('niiu-games-library', {
    props: [],
    template: template,
    data: function () {
      return {
        user: {}
      }
    },
    mounted: async function () {

    }
  });

})();
