package com.gmc.websocket.service;

import javax.websocket.Session;
import java.util.HashMap;

public interface WebSocketService {

    Session okexWebSocket();

    Session upbitWebSocket();
    HashMap<Object, Object> getUpbitOrderBookResult();
}
