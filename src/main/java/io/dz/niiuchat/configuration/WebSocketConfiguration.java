package io.dz.niiuchat.configuration;

import io.dz.niiuchat.websocket.LiveSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final LiveSocketHandler liveSocketHandler;

    public WebSocketConfiguration(LiveSocketHandler liveSocketHandler) {
        this.liveSocketHandler = liveSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(liveSocketHandler, "/live")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

}
