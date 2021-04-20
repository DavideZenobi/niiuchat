(() => {

  const template = `
    <div class="button-animation-area"
         v-show="show">
        <img class="animate__animated animate__flipInX"
             src="/images/check.svg"
             alt="check">
    </div>
  `;

  Vue.component('niiu-check-animation', {
    props: ['show'],
    template: template,
    data: function () {
      return { }
    }
  });

})();
