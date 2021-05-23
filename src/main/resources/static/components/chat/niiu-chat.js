(() => {

    const template = `
        <div class="main-area-container">
          <md-content class="chat-message-list md-scrollbar">
              <div style="display: flex; flex-direction: column-reverse;">
                  <div v-for="messageData in messages" :key="messageData.message.id"
                       :style="getParentStyle(messageData.message)">
                      <niiu-chat-message :message-data="messageData"></niiu-chat-message>
                  </div>
              </div>
          </md-content>
          
          <div style="padding: 1rem;">
            <niiu-chat-input :group-id="groupId" @on-send-message="(message) => messages.unshift(message)"></niiu-chat-input>
          </div>
        </div>
    `;

    Vue.component('niiu-chat', {
        props: ['groupId'],
        template: template,
        data: function () {
            return {
                messages: [],
                currentUser: {}
            }
        },
        mounted: async function () {
            const response = await UserApi.getCurrentUser();
            this.currentUser = response.data;

            this.reloadMessages();

            // Check messages received
            PubSub.subscribe(NiiuEvents.MESSAGE_RECEIVED, (topic, msg) => {
                if (msg.groupId === this.groupId) {
                    this.reloadMessages();
                }
            });
        },
        methods: {
            reloadMessages: async function () {
                const response = await ChatsApi.getMessagesByGroupId({
                    groupId: this.groupId,
                    limit: 20,
                    offset: 0
                });

                this.messages = response.data;
            },
            getParentStyle: function (message) {
                return {
                    display: 'flex',
                    'flex-direction': message.userId === this.currentUser.id ? 'row-reverse' : 'row'
                };
            }
        }
    });

})();
