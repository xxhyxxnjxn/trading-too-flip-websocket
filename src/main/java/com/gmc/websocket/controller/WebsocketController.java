package com.gmc.websocket.controller;

import com.gmc.websocket.service.WebSocketService;
import com.gmc.websocket.upbit.UpbitWebSocketClientEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebsocketController {

    private final WebSocketService webSocketService;

    @GetMapping("/websocket")
    public void websocketTest(){
        System.out.println("test : "+webSocketService.getUpbitOrderBookResult());
    }
}
