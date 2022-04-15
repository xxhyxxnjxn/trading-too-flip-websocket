package com.gmc.redis.controller;

import com.gmc.redis.entity.WebSocketOrderbook;
import com.gmc.redis.repository.WebSocketOrderbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webSocketAll")
public class RedisController {
    private final WebSocketOrderbookRepository webSocketOrderbookRepository;

    @GetMapping
    public List<WebSocketOrderbook> getAllProducts() {
        return webSocketOrderbookRepository.findAll();
    }

}

