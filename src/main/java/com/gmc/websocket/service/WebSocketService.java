package com.gmc.websocket.service;

import javax.websocket.Session;

public interface WebSocketService {

    Session okexWebSocket();

    Session upbitWebSocket();

    void getTest();

}
