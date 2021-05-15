package io.dz.niiuchat.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class LiveService {

    private final WsSessions wsSessions;

    public LiveService(WsSessions wsSessions) {
        this.wsSessions = wsSessions;
    }

    // Commented for now
    /*public boolean sessionExistsAndIsAlive(Long key) {
        return wsSessions.get(key)
                .map(WebSocketSession::isOpen)
                .orElse(false);
    }*/

    public void putSession(Long key, WebSocketSession session) {
        wsSessions.put(key, session);
    }

    public Optional<WebSocketSession> removeSession(Long key) {
        return Optional.ofNullable(wsSessions.remove(key));
    }

    public void forEachSession(BiConsumer<Long, WebSocketSession> action) {
        wsSessions.forEach(action);
    }

}
