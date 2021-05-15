package io.dz.niiuchat.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class WsSessions {

    private final Map<Long, WebSocketSession> sessions = Collections.synchronizedMap(new HashMap<>());

    public Optional<WebSocketSession> get(Long key) {
        return Optional.ofNullable(sessions.get(key));
    }

    public void put(Long key, WebSocketSession session) {
        sessions.put(key, session);
    }

    public boolean exists(Long key) {
        return sessions.containsKey(key);
    }

    public WebSocketSession remove(Long key) {
        return sessions.remove(key);
    }

    public void forEach(BiConsumer<Long, WebSocketSession> action) {
        sessions.forEach(action);
    }

}
