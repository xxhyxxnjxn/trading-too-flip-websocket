package com.gmc.websocket.upbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmc.websocket.config.BeanUtils;
import com.gmc.websocket.service.WebSocketService;
import com.gmc.websocket.service.impl.WebSocketServiceImpl;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
@Service
public class UpbitWebSocketClientEndpoint {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Session session = null;
    private WebSocketService webSocketService;
    String test;

    @OnOpen
    @SneakyThrows
    public void onOpen(Session session) {
        System.out.println("onOpen");

        String sendText = createSendText();

        session.getBasicRemote().sendText(sendText);

        this.session = session;

        sendMessage();
    }


    public void sendMessage() {
        executor.submit(() -> {
            while (true) {

                this.session.getAsyncRemote().sendText("ping");
                TimeUnit.SECONDS.sleep(20);
            }
        });

    }

    private String createSendText() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ticket", "test");

        JSONArray codeArray = new JSONArray();
        codeArray.put("KRW" + "-" + "BTC");

        JSONObject jsonOrderbookObject = new JSONObject();
        jsonOrderbookObject.put("type", "orderbook");
        jsonOrderbookObject.put("codes", codeArray);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        jsonArray.put(jsonOrderbookObject);
        return jsonArray.toString();
    }

    @OnMessage
    @SneakyThrows
    public void onMessage(Session session, ByteBuffer message) throws JsonProcessingException {

        byte[] rtnBytes = message.array();
        String rtnStr = new String(rtnBytes);

        System.out.println(rtnStr);
        this.test = rtnStr;

        if (!rtnStr.equals("pong")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(rtnStr);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("upbit websocket onClose");

        this.executor.shutdown();

        webSocketService = (WebSocketServiceImpl) BeanUtils.getBean(WebSocketServiceImpl.class);
    }

    public String getTest(){
        return test;
    }
}
