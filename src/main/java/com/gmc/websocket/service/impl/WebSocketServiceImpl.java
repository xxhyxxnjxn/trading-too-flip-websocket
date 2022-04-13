package com.gmc.websocket.service.impl;

import com.gmc.websocket.okex.OkexWebSocketClientEndpoint;
import com.gmc.websocket.service.WebSocketService;
import com.gmc.websocket.upbit.UpbitWebSocketClientEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final WebsocketTest websocketTest;

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

    @Override
    public Session upbitWebSocket() {
        try {
            String uri = "wss://api.upbit.com/websocket/v1";
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            final Session session = container.connectToServer(websocketTest.startWebsocket(), URI.create(uri));
            return session;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getTest() {
        System.out.println("test : "+websocketTest.printWebsocketResult());
    }


}
