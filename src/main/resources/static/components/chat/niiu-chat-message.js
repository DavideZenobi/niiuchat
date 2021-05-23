(() => {

    const template = `
        <div>
          <div :style="getItemStyle()">
              <div v-show="messageData.message.hasAttachment" style="margin-bottom: 1rem; text-align: center; width: 100%;">
                <a :href="attachmentUrl" download=""><md-icon class="md-size-2x md-primary">file_present</md-icon></a>
              </div>
              <span>{{messageBody}}</span>
          </div>
        </div>
    `;

    Vue.component('niiu-chat-message', {
        props: ['messageData'],
        template: template,
        data: function () {
            return {
            }
        },
        computed: {
            messageBody: function () {
                let message = this.messageData.message.message;

                return message;
            },
            attachmentUrl: function () {
                return this.messageData.message.hasAttachment ?
                    `/api/files/${this.messageData.attachment.fileId}` :
                    '';
            }
        },
        methods: {
            getItemStyle: function () {
                return {
                    border: 'solid 1px #000000',
                    'border-radius': '4px',
                    padding: '.75rem',
                    margin: '.5rem',
                    width: 'fit-content'
                };
            }
        }
    });

})();
