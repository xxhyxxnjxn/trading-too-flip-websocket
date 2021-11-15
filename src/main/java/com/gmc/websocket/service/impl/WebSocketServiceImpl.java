package com.gmc.websocket.service.impl;

import com.gmc.websocket.okex.OkexWebSocketClientEndpoint;
import com.gmc.websocket.repository.WebSocketOrderbookRepository;
import com.gmc.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final WebSocketOrderbookRepository webSocketOrderbookRepository;

    public WebSocketServiceImpl(WebSocketOrderbookRepository webSocketOrderbookRepository) {
        this.webSocketOrderbookRepository = webSocketOrderbookRepository;
    }

    @Override
    public Session okexWebSocket() {
        try {
            String uri = "wss://wsaws.okex.com:8443/ws/v5/public/";
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.setDefaultMaxBinaryMessageBufferSize(1048576);
            container.setDefaultMaxTextMessageBufferSize(1048576);
            final Session session = container.connectToServer(OkexWebSocketClientEndpoint.class, URI.create(uri));
            return session;
        } catch (Exception e) {
            log.info("okexWebSocket exceptions");
            log.info(String.valueOf(e));

            throw new RuntimeException(e);
        }
    }


}
