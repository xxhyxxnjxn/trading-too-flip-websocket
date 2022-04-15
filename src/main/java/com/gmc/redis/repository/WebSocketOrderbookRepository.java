package com.gmc.redis.repository;

import com.gmc.redis.entity.WebSocketOrderbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WebSocketOrderbookRepository {
    private static final String HASH_KEY = "WebSocketOrderbook";
    @Autowired
    private RedisTemplate redisTemplate;

    public WebSocketOrderbook save(WebSocketOrderbook webSocketOrderbook) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        map.put(webSocketOrderbook.getMarket(), webSocketOrderbook.getJsonData());
        hashOperations.putAll(HASH_KEY, map);
        return webSocketOrderbook;
    }

    public WebSocketOrderbook update(WebSocketOrderbook webSocketOrderbook) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        map.put(webSocketOrderbook.getMarket(), webSocketOrderbook.getJsonData());
        hashOperations.putAll(HASH_KEY, map);
        return webSocketOrderbook;
    }

    public List<WebSocketOrderbook> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public WebSocketOrderbook findProductByMarket(String market) {
        String data = (String) redisTemplate.opsForHash().get(HASH_KEY, market);
        WebSocketOrderbook webSocketOrderbook = WebSocketOrderbook.builder()
                .jsonData(data)
                .market(market)
                .build();
        return webSocketOrderbook;
    }

}
