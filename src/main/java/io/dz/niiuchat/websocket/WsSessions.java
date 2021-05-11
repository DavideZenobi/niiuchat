package io.dz.niiuchat.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class WsSessions {

    private final Map<String, WebSocketSession> sessions = Collections.synchronizedMap(new HashMap<>());

    public void put(String key, WebSocketSession session) {
        sessions.put(key, session);
    }

    public boolean exists(String key) {
        return sessions.containsKey(key);
    }

    public WebSocketSession remove(String key) {
        return sessions.remove(key);
    }

    public void forEach(BiConsumer<String, WebSocketSession> action) {
        sessions.forEach(action);
    }

}
