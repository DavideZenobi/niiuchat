package io.dz.niiuchat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.websocket.dto.LiveMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;

@Service
public class LiveSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiveSocketHandler.class);

    private final ObjectMapper mapper;
    private final LiveService liveService;

    public LiveSocketHandler(ObjectMapper mapper, LiveService liveService) {
        this.mapper = mapper;
        this.liveService = liveService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        var principal = session.getPrincipal();

        if (principal == null) {
            session.close();
            LOGGER.warn("Socket {} not authenticated", session.getId());
            return;
        }

        var niiuUser = NiiuUser.from(principal);
        String username = niiuUser.getUser().getUsername();

        if (liveService.sessionExists(niiuUser.getUsername())) {
            session.close();
            LOGGER.warn("User {} already connected", username);
            return;
        }

        LOGGER.info("User {} connected to websocket", username);

        var liveMessage = LiveMessage.createUserConnectedMessage(niiuUser.getUser().getId());
        var textMessage = new TextMessage(mapper.writeValueAsBytes(liveMessage));

        notifyUserConnected(textMessage);

        liveService.putSession(username, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // To be implemented if needed
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var principal = session.getPrincipal();

        if (principal == null) {
            session.close();
            return;
        }

        var niiuUser = NiiuUser.from(principal);
        String username = niiuUser.getUser().getUsername();

        session.close();
        liveService.removeSession(username);
        LOGGER.info("User {} disconnected", niiuUser.getUser().getUsername());
    }

    private void notifyUserConnected(TextMessage textMessage) {
        liveService.forEachSession((username, session) -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                LOGGER.error("Error sending userConnectedMessage", e);
            }
        });
    }

}
