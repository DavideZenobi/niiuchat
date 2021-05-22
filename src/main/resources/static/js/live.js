(() => {

    const Messages = {
        CONNECTION_ESTABLISHED: 'CONNECTION_ESTABLISHED',
        CONNECTION_CLOSED: 'CONNECTION_CLOSED',
        MESSAGE_RECEIVED: 'MESSAGE_RECEIVED',
        USER_CONNECTED: 'USER_CONNECTED',
        GROUP_CREATED: 'GROUP_CREATED',
        GROUP_DELETED: 'GROUP_DELETED'
    };

    class NiiuLive {

        connect() {
            const liveSocket = new WebSocket(`${window.location.protocol.replace('http', 'ws')}//${window.location.host}/live`);

            liveSocket.onopen = this.onOpen;
            liveSocket.onclose = this.onClose;
            liveSocket.onmessage = this.onMessage;
        }

        onOpen(event) {
            PubSub.publish(NiiuEvents.CONNECTION_ESTABLISHED, true);
        }

        onClose(event) {
            PubSub.publish(NiiuEvents.CONNECTION_CLOSED, true)
        }

        onMessage(event) {
            const message = JSON.parse(event.data);

            if (message.type === Messages.USER_CONNECTED) {
                PubSub.publish(NiiuEvents.USER_CONNECTED, message.content);
            } else if (message.type === Messages.GROUP_CREATED) {
                PubSub.publish(NiiuEvents.GROUP_CREATED, message.content.groupId);
            }  else if (message.type === Messages.MESSAGE_RECEIVED) {
                PubSub.publish(NiiuEvents.MESSAGE_RECEIVED, message.content);
            }  else if (message.type === Messages.GROUP_DELETED) {
                PubSub.publish(NiiuEvents.GROUP_DELETED, message.content);
            } else {
                console.log(`Unhandled message ${event.data}`);
            }
        }

    }

    window.NiiuLive = new NiiuLive();

})();
