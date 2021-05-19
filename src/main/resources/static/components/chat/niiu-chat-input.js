(() => {

  const template = `
    <div>
      <md-dialog :md-active.sync="showDialog">
        <md-dialog-title>File</md-dialog-title>
        <md-field>
          <md-input v-model="dialog.message" placeholder="A nice placeholder"></md-input>
          <md-dialog-actions>
            <md-button class="md-icon-button" 
                       @click="showDialog = false"
                       :disabled="!isTypingFile">Send</md-button>
            <md-button class="md-icon-button" @click="showDialog = false">Close</md-button>
          </md-dialog-actions>
        </md-field>
      </md-dialog>
      <md-field>
        <md-button class="md-icon-button" @click="showDialog = true">
          <md-icon class="md-icon-primary">attach_file</md-icon>
        </md-button>
        <md-input v-model="message" placeholder="A nice placeholder"></md-input>
        <md-button class="md-icon-button" 
                   @click="sendMessage"
                   :disabled="!isTypingMessage">
          <md-icon class="md-icon-primary">send</md-icon>
        </md-button>
      </md-field>
    </div>
  `;

  Vue.component('niiu-chat-input', {
    props: ['groupId'],
    template: template,
    data: function () {
      return {
        dialog: {
          message: '',
          file: ''
        },
        message: '',
        showDialog: false
      }
    },
    computed: {
      isTypingMessage: function () {
        return this.message;
      },
      isTypingFile: function () {
        return this.dialog.message;
      }
    },
    methods: {
      sendMessage: async function () {
        const data = {
          groupId: this.groupId,
          message: this.message,
          hasAttachment: false
        };
        const response = await ChatsApi.sendMessage(data);
        this.$emit('on-send-message', response.data);
        this.message = '';


      }
    }
  });

})();
