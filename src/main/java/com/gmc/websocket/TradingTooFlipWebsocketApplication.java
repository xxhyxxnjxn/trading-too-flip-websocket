package com.gmc.websocket;

import com.gmc.websocket.service.WebSocketService;
import com.gmc.websocket.service.impl.WebsocketTest;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
public class TradingTooFlipWebsocketApplication {
    private final WebSocketService webSocketService;
    private final WebsocketTest websocketTest;

    public TradingTooFlipWebsocketApplication(WebSocketService webSocketService, WebsocketTest websocketTest) {
        this.webSocketService = webSocketService;
        this.websocketTest = websocketTest;
    }

    public static void main(String[] args) {
        SpringApplication.run(TradingTooFlipWebsocketApplication.class, args);
    }

    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    void applicaitonReady() throws DeploymentException, IOException {
        webSocketService.upbitWebSocket();
    }
}
