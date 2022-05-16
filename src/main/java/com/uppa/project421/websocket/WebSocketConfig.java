package com.uppa.project421.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private HttpHandshakeInterceptor handshakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-text").withSockJS().setInterceptors(handshakeInterceptor);
        registry.addEndpoint("/connect").withSockJS().setInterceptors(handshakeInterceptor);
        registry.addEndpoint("/lobby").withSockJS().setInterceptors(handshakeInterceptor);
        registry.addEndpoint("/game").withSockJS().setInterceptors(handshakeInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

}
