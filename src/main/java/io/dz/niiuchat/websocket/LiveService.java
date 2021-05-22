package io.dz.niiuchat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dz.niiuchat.websocket.dto.LiveMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class LiveService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LiveService.class);

    private final WsSessions wsSessions;
    private final ObjectMapper mapper;

    public LiveService(WsSessions wsSessions, ObjectMapper mapper) {
        this.wsSessions = wsSessions;
        this.mapper = mapper;
    }

    @Async("liveExecutor")
    public void sendMessage(Collection<Long> userIds, LiveMessage liveMessage) {
        for (Long userId : userIds) {
            wsSessions.get(userId).ifPresent(session -> sendMessage(session, userId, liveMessage));
        }
    }

    private void sendMessage(WebSocketSession session, Long userId, LiveMessage liveMessage) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(mapper.writeValueAsBytes(liveMessage)));
            } else {
                wsSessions.remove(userId);
            }
        } catch (IOException e) {
            LOGGER.error("Error sending message to session {}", session, e);
        }
    }

    void putSession(Long key, WebSocketSession session) {
        wsSessions.put(key, session);
    }

    Optional<WebSocketSession> removeSession(Long key) {
        return Optional.ofNullable(wsSessions.remove(key));
    }

    void forEachSession(BiConsumer<Long, WebSocketSession> action) {
        wsSessions.forEach(action);
    }

}
