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
                            <span>{{message}}</span>
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
                messages: [
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef',
                    'abcdefewfwefwef'
                ]
            }
        },
        mounted: async function () {
            const response = await ChatsApi.getMessagesByGroupId();
            // this.messages = response.data;
        },
        methods: {
            getParentStyle: function (item, index) {
                return {
                    display: 'flex',
                    'flex-direction': index % 2 === 0 ? 'row' : 'row-reverse'
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
