package io.dz.niiuchat.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.BiConsumer;

@Service
public class LiveService {

    private final WsSessions wsSessions;

    public LiveService(WsSessions wsSessions) {
        this.wsSessions = wsSessions;
    }

    public boolean sessionExists(String key) {
        return wsSessions.exists(key);
    }

    public void putSession(String key, WebSocketSession session) {
        wsSessions.put(key, session);
    }

    public void removeSession(String key) {
        wsSessions.remove(key);
    }

    public void forEachSession(BiConsumer<String, WebSocketSession> action) {
        wsSessions.forEach(action);
    }

}
