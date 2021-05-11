(() => {

    const Messages = {
        CONNECTION_ESTABLISHED: 'CONNECTION_ESTABLISHED',
        CONNECTION_CLOSED: 'CONNECTION_CLOSED',
        MESSAGE_RECEIVED: 'MESSAGE_RECEIVED',
        USER_CONNECTED: 'USER_CONNECTED'
    };

    class NiiuLive {

        connect() {
            const liveSocket = new WebSocket(`${window.location.protocol.replace('http', 'ws')}//${window.location.host}/live`);

            liveSocket.onopen = this.onOpen;
            liveSocket.onclose = this.onClose;
            liveSocket.onmessage = this.onMessage;
        }

        onOpen(event) {
            PubSub.publish(Messages.CONNECTION_ESTABLISHED, true);
        }

        onClose(event) {
            PubSub.publish(Messages.CONNECTION_CLOSED, true)
        }

        onMessage(event) {
            const message = JSON.parse(event.data);

            if (message.type === Messages.USER_CONNECTED) {
                PubSub.publish(Messages.USER_CONNECTED, message);
            }
        }

    }

    NiiuLive.prototype.Messages = Messages;

    window.NiiuLive = new NiiuLive();

})();
