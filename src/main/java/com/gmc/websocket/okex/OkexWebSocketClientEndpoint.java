package com.gmc.websocket.okex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmc.websocket.config.BeanUtils;
import com.gmc.websocket.domain.FlipCurrency;
import com.gmc.websocket.domain.WebSocketOrderbook;
import com.gmc.websocket.domain.WebSocketTicker;
import com.gmc.websocket.repository.FlipCurrencyRepository;
import com.gmc.websocket.repository.WebSocketOrderbookRepository;
import com.gmc.websocket.repository.WebSocketTickerRepository;
import com.gmc.websocket.service.WebSocketService;
import com.gmc.websocket.service.impl.WebSocketServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
@Component
@Slf4j
public class OkexWebSocketClientEndpoint {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    List<FlipCurrency> currencyList;
    JSONObject orderbook = new JSONObject();
    Session session = null;
    private WebSocketOrderbookRepository webSocketOrderbookRepository;
    private WebSocketTickerRepository webSocketTickerRepository;
    private FlipCurrencyRepository currencyRepository;
    private WebSocketService webSocketService;

    @OnOpen
    @SneakyThrows
    public void onOpen(Session session) {
        log.info("onOpen");
        currencyRepository = (FlipCurrencyRepository) BeanUtils.getBean(FlipCurrencyRepository.class);
        webSocketTickerRepository = (WebSocketTickerRepository) BeanUtils.getBean(WebSocketTickerRepository.class);
        webSocketOrderbookRepository = (WebSocketOrderbookRepository) BeanUtils.getBean(WebSocketOrderbookRepository.class);
        this.session = session;
        log.info(String.valueOf(session));
        String sendText = createSendText();
        this.session.getBasicRemote().sendText(sendText);
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
        currencyList = currencyRepository.findByFlipExchange_FlipExchangeName("okex");
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (FlipCurrency currency : currencyList) {
            String market = currency.getFlipCurrencyName().toUpperCase() + "-" + currency.getFlipPayment().toUpperCase();
            JSONObject tickerObject = new JSONObject();
            tickerObject.put("channel", "tickers");
            tickerObject.put("instId", market);
            jsonArray.put(tickerObject);
            JSONObject orderbookObject = new JSONObject();
            orderbookObject.put("channel", "books5");
            orderbookObject.put("instId", market);
            jsonArray.put(orderbookObject);
        }
        jsonObject.put("op", "subscribe");
        jsonObject.put("args", jsonArray);
        return jsonObject.toString();
    }

    @OnMessage
    @SneakyThrows
    public void onMessage(Session session, String message) {
        log.info(message);
        if (!message.equals("pong")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);
            if (jsonNode.path("event").isMissingNode()) {
                String[] market = jsonNode.path("arg").path("instId").asText().split("-");
                FlipCurrency findCurrency = currencyList.stream()
                        .filter(currency -> currency.getFlipCurrencyName().equals(market[0]))
                        .filter(currency -> currency.getFlipPayment().equals(market[1])).findFirst().get();
                if (jsonNode.path("arg").path("channel").asText().equals("tickers")) {
                    WebSocketTicker webSocketTicker = webSocketTickerRepository.findByFlipCurrency(findCurrency);
                    if (webSocketTicker == null) {
                        webSocketTicker = WebSocketTicker.builder()
                                .flipCurrency(findCurrency)
                                .jsonData(message)
                                .timestamp(LocalDateTime.now())
                                .build();
                        webSocketTickerRepository.save(webSocketTicker);
                    } else {
                        webSocketTicker.setFlipCurrency(findCurrency);
                        webSocketTicker.setJsonData(message);
                        webSocketTicker.setTimestamp(LocalDateTime.now());
                        webSocketTickerRepository.save(webSocketTicker);
                    }
                } else if (jsonNode.path("arg").path("channel").asText().equals("books5")) {
                    WebSocketOrderbook webSocketOrderbook = webSocketOrderbookRepository.findByFlipCurrency(findCurrency);
                    if (webSocketOrderbook == null) {
                        webSocketOrderbook = WebSocketOrderbook.builder()
                                .flipCurrency(findCurrency)
                                .jsonData(message)
                                .timestamp(LocalDateTime.now())
                                .build();
                        webSocketOrderbookRepository.save(webSocketOrderbook);
                    } else {
                        webSocketOrderbook.setFlipCurrency(findCurrency);
                        webSocketOrderbook.setJsonData(message);
                        webSocketOrderbook.setTimestamp(LocalDateTime.now());
                        webSocketOrderbookRepository.save(webSocketOrderbook);
                    }
                }
            }
        }
    }

    private void book400(ObjectMapper mapper, JsonNode jsonNode, FlipCurrency findCurrency) throws JSONException, JsonProcessingException {
        WebSocketOrderbook webSocketOrderbook = webSocketOrderbookRepository.findByFlipCurrency(findCurrency);
        if (webSocketOrderbook == null) {
            //처음 저장은 스냅샷
            if (jsonNode.path("action").asText().equals("snapshot")) {
                List<JsonNode> beforeAsks = new ArrayList<>();
                List<JsonNode> beforeBids = new ArrayList<>();
                JsonNode askList = jsonNode.path("data").path(0).path("asks");
                JsonNode bidList = jsonNode.path("data").path(0).path("bids");
                for (JsonNode ask : askList) {
                    beforeAsks.add(ask);
                }
                for (JsonNode bid : bidList) {

                    beforeBids.add(bid);
                }
                orderbook.put("asks", beforeAsks);
                orderbook.put("bids", beforeBids);
                webSocketOrderbook = WebSocketOrderbook.builder()
                        .flipCurrency(findCurrency)
                        .jsonData(orderbook.toString())
                        .timestamp(LocalDateTime.now())
                        .build();
                webSocketOrderbookRepository.save(webSocketOrderbook);
            }
        } else {
            // 디비에 값이 있을때
            List<JsonNode> beforeAsks = new ArrayList<>();
            List<JsonNode> beforeBids = new ArrayList<>();
            if (jsonNode.path("action").asText().equals("snapshot")) {
                JsonNode askList = jsonNode.path("data").path(0).path("asks");
                JsonNode bidList = jsonNode.path("data").path(0).path("bids");
                for (JsonNode ask : askList) {
                    beforeAsks.add(ask);
                }
                for (JsonNode bid : bidList) {
                    beforeBids.add(bid);
                }
            } else if (jsonNode.path("action").asText().equals("update")) {
                JsonNode beforeData = mapper.readTree(webSocketOrderbook.getJsonData());
                JsonNode beforeAsksJson = mapper.readTree(beforeData.get("asks").asText());
                JsonNode beforeBidsJson = mapper.readTree(beforeData.get("bids").asText());
                for (JsonNode beforeAsk : beforeAsksJson) {
                    beforeAsks.add(beforeAsk);
                }
                for (JsonNode beforeBid : beforeBidsJson) {
                    beforeBids.add(beforeBid);
                }
                JsonNode askList = jsonNode.path("data").path(0).path("asks");
                JsonNode bidList = jsonNode.path("data").path(0).path("bids");
                for (JsonNode ask : askList) {
                    for (int i = 0; i < beforeAsks.size(); i++) {
                        if (ask.path(0).asDouble() > beforeAsks.get(i).path(0).asDouble()) {
                            continue;
                        }
                        if (ask.path(0).asDouble() == beforeAsks.get(i).path(0).asDouble()) {
                            if (ask.path(1).asDouble() == 0) {
                                beforeAsks.remove(beforeAsks.get(i));
                            } else {
                                beforeAsks.set(i, ask);
                            }
                            break;
                        }
                        if (ask.path(0).asDouble() < beforeAsks.get(i).path(0).asDouble()) {
                            beforeAsks.add(i, ask);
                            break;
                        }
                    }
                }
                for (JsonNode bid : bidList) {
                    for (int i = 0; i < beforeBids.size(); i++) {
                        if (bid.path(0).asDouble() < beforeBids.get(i).path(0).asDouble()) {
                            continue;
                        }
                        if (bid.path(0).asDouble() == beforeBids.get(i).path(0).asDouble()) {
                            if (bid.path(1).asDouble() == 0) {
                                beforeBids.remove(beforeBids.get(i));
                            } else {
                                beforeBids.set(i, bid);
                            }
                            break;
                        }
                        if (bid.path(0).asDouble() > beforeBids.get(i).path(0).asDouble()) {
                            beforeBids.add(i, bid);
                            break;
                        }
                    }
                }
            }
            webSocketOrderbook.setFlipCurrency(findCurrency);
            webSocketOrderbook.setJsonData(orderbook.toString());
            webSocketOrderbook.setTimestamp(LocalDateTime.now());
            webSocketOrderbookRepository.save(webSocketOrderbook);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("okex websocket onError");
        log.info(String.valueOf(throwable));
        log.info(throwable.getMessage());
        throwable.printStackTrace();
    }

    @OnClose
    @SneakyThrows
    public void onClose(Session session) {
        log.info("okex websocket onClose");
        this.executor.shutdownNow();

        TimeUnit.SECONDS.sleep(1000);

        webSocketService = (WebSocketServiceImpl) BeanUtils.getBean(WebSocketServiceImpl.class);
        webSocketService.okexWebSocket();
    }

}
