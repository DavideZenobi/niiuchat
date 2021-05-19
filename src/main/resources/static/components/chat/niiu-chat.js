(() => {

    const template = `
        <div class="main-area-container">
            <md-content class="chat-message-list md-scrollbar">
                <div style="display: flex; flex-direction: column;">
                    <div v-for="message in messages" :key="message.id"
                         :style="getParentStyle(message)">
                        <div :style="getItemStyle(message)">
                            <span>{{message.message}}</span>
                        </div>
                    </div>
                </div>
            </md-content>
          <niiu-chat-input :group-id="groupId"></niiu-chat-input>
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
            const responses = await Promise.all([
                ChatsApi.getMessagesByGroupId({
                    groupId: this.groupId,
                    limit: 20,
                    offset: 0
                }),
                UserApi.getCurrentUser()
            ]);

            this.messages = responses[0].data;
            this.currentUser = responses[1].data;
        },
        methods: {
            getParentStyle: function (message) {
                return {
                    display: 'flex',
                    'flex-direction': message.userId === this.currentUser.id ? 'row-reverse' : 'row'
                };
            },
            getItemStyle: function (message) {
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
