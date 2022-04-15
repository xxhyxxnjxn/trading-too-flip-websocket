package com.gmc;

import com.gmc.websocket.service.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TradingTooFlipWebsocketApplication {
    private final WebSocketService webSocketService;

    public TradingTooFlipWebsocketApplication(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TradingTooFlipWebsocketApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    void applicaitonReady() {
        webSocketService.upbitWebSocket();
    }
}
