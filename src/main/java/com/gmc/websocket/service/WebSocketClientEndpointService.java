package com.gmc.websocket.service;

import com.gmc.websocket.upbit.UpbitWebSocketClientEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketClientEndpointService {
    private static final String UPBIT_WEBSOCKET_URL = "wss://api.upbit.com/websocket/v1";

    private final UpbitWebSocketClientEndpoint upbitWebSocketClientEndpoint;

    public Session startUpbitWebsocket() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            final Session session = container.connectToServer(upbitWebSocketClientEndpoint, URI.create(UPBIT_WEBSOCKET_URL));
            return session;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public HashMap<Object, Object> getUpbitOrderBookResult(){
        return upbitWebSocketClientEndpoint.getOrderBookResult();
    }
}
