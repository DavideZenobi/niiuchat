(() => {

  const template = `
    <div>
      <md-dialog :md-active.sync="showDialog">
        <form novalidate
              class="md-layout"
              style="padding: 1rem;"
              @submit.prevent="sendAttachment">
          <div @click="$refs.attachmentInput.click()"
               style="cursor: pointer; text-align: center; width: 100%;">
            <md-icon class="md-size-5x md-primary">upload</md-icon>
          </div>
          
          <input ref="attachmentInput"
               type="file"
               accept="*/*"
               v-show="false"
               @change="onFileSelected" />
          
          <md-field>
            <label></label>
            <md-input v-model="dialog.message" placeholder="Message ..."></md-input>
          </md-field>
               
          <md-dialog-actions>
            <md-button class="md-icon-button" 
                       @click="sendAttachment"
                       :disabled="!isTypingFile">Send</md-button>
            <md-button class="md-icon-button" @click="onCloseDialog">Close</md-button>
          </md-dialog-actions>
        </form>
      </md-dialog>
      
      <form novalidate
            class="md-layout"
            @submit.prevent="sendMessage">
        <md-field>
          <md-button class="md-icon-button" @click="showDialog = true">
            <md-icon class="md-icon-primary">attach_file</md-icon>
          </md-button>
          
          <md-input v-model="message" placeholder="Message ..."></md-input>
          
          <md-button class="md-icon-button" 
                     @click="sendMessage"
                     :disabled="!isTypingMessage">
            <md-icon class="md-icon-primary">send</md-icon>
          </md-button>
        </md-field>
      </form>
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
      sendAttachment: async function () {
        if (!this.dialog.message || !this.dialog.file) {
          return;
        }

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
