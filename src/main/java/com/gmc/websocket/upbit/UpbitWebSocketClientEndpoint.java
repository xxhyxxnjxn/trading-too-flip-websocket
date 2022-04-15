package com.gmc.websocket.upbit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmc.redis.entity.WebSocketOrderbook;
import com.gmc.redis.repository.WebSocketOrderbookRepository;
import com.gmc.websocket.config.BeanUtils;
import com.gmc.websocket.domain.FlipCurrency;
import com.gmc.websocket.repository.FlipCurrencyRepository;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
@Service
public class UpbitWebSocketClientEndpoint {
    private FlipCurrencyRepository currencyRepository;
    private WebSocketOrderbookRepository webSocketOrderbookRepository;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Session session = null;
    List<FlipCurrency> currencyList;
    HashMap<Object, Object> orderBookResult = new HashMap<>();

    @OnOpen
    @SneakyThrows
    public void onOpen(Session session) {
        System.out.println("onOpen");
        webSocketOrderbookRepository = (WebSocketOrderbookRepository) BeanUtils.getBean(WebSocketOrderbookRepository.class);
        currencyRepository = (FlipCurrencyRepository) BeanUtils.getBean(FlipCurrencyRepository.class);

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
        currencyList = currencyRepository.findByFlipExchange_FlipExchangeName("upbit");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ticket", "test");

        JSONArray codeArray = new JSONArray();
        for (FlipCurrency currency : currencyList) {
            codeArray.put(currency.getFlipPayment().toUpperCase() + "-" + currency.getFlipCurrencyName().toUpperCase());
        }

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

//        System.out.println(rtnStr);
        if (!rtnStr.equals("pong")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(rtnStr);
            String market = jsonNode.path("code").asText();

//            this.orderBookResult.put(market, rtnStr);
            WebSocketOrderbook webSocketOrderbook = webSocketOrderbookRepository.findProductByMarket(market);
            if (webSocketOrderbook.getJsonData() == null) {
                webSocketOrderbook = WebSocketOrderbook.builder()
                        .jsonData(rtnStr)
                        .market(market)
                        .build();
                webSocketOrderbookRepository.save(webSocketOrderbook);
            } else {
                webSocketOrderbook.setJsonData(rtnStr);
                webSocketOrderbookRepository.update(webSocketOrderbook);

            }

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
    }

    public HashMap<Object, Object> getOrderBookResult() {
        return orderBookResult;
    }
}
