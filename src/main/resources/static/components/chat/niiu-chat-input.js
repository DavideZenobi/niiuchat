(() => {

  const template = `
    <div>
      <md-dialog :md-active.sync="showDialog">
        <md-dialog-title>File</md-dialog-title>
        <md-button class="md-icon-button" @click="$refs.attachmentInput.click()">
          <md-icon>upload</md-icon>
        </md-button>
        
        <md-input v-model="dialog.message" placeholder="Message..."></md-input>
        <input ref="attachmentInput"
             type="file"
             accept="*/*"
             v-show="false"
             @change="onFileSelected" />
             
        <md-dialog-actions>
          <md-button class="md-icon-button" 
                       @click="onAttachmentSend"
                       :disabled="!isTypingFile">Send</md-button>
        <md-button class="md-icon-button" @click="onCloseDialog">Close</md-button>
        </md-dialog-actions>
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
        const formData = new FormData();
        formData.append('groupId', this.groupId);
        formData.append('message', this.message);

        const response = await ChatsApi.sendMessage(formData);
        this.$emit('on-send-message', response.data);
        this.message = '';
      },
      onCloseDialog: function() {
        this.dialog.message = '';
        this.dialog.file = null;

        this.showDialog = false;
      },
      onFileSelected: function(event) {
        const files = event.target.files;

        if (files.length >= 1) {
          this.dialog.file = files[0];
        }
      },
      onAttachmentSend: async function () {
        const formData = new FormData();
        formData.append('groupId', this.groupId);
        formData.append('message', this.dialog.message);

        if (this.dialog.file) {
          formData.append('attachment', this.dialog.file);
        }

        const response = await ChatsApi.sendMessage(formData);
        this.$emit('on-send-message', response.data);
        this.dialog.message = '';
        this.dialog.file = null;

        this.showDialog = false;
      }
    }
  });

})();
