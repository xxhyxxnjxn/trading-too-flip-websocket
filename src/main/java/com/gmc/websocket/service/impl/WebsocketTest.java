package com.gmc.websocket.service.impl;

import com.gmc.websocket.upbit.UpbitWebSocketClientEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

@Service
@RequiredArgsConstructor
public class WebsocketTest {
    private final UpbitWebSocketClientEndpoint upbitWebSocketClientEndpoint;

    public UpbitWebSocketClientEndpoint startWebsocket(){
        return upbitWebSocketClientEndpoint;
    }

    public String printWebsocketResult(){
        return upbitWebSocketClientEndpoint.getTest();
    }
}
