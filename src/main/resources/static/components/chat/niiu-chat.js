(() => {

    const template = `
        <div class="main-area-container">
            <md-content class="chat-message-list md-scrollbar">
                <div style="display: flex; flex-direction: column;">
                    <div :style="getParentStyle(0, 0)">
                        <div :style="getItemStyle(0, 0)">
                            <span>HOLA {{groupId}}</span>
                        </div>
                    </div>
                    <div v-for="(message, index) in messages" :key="index"
                         :style="getParentStyle(message, index)">
                        <div :style="getItemStyle(message, index)">
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
            const response = await ChatsApi.getMessagesByGroupId(this.groupId);
            this.messages = response.data;
            const user = await UserApi.getCurrentUser();
            this.currentUser = user.data;
        },
        methods: {
            getParentStyle: function (item, index) {
                return {
                    display: 'flex',
                    'flex-direction': item.userId === this.currentUser.id ? 'row-reverse' : 'row'
                };
            },
            getItemStyle: function (item, index) {
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
