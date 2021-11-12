package com.gmc.websocket;

import com.gmc.websocket.service.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import javax.websocket.DeploymentException;
import java.io.IOException;

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
    void applicaitonReady() throws DeploymentException, IOException {
        webSocketService.okexWebSocket();
    }
}
