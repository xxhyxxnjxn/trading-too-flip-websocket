package com.gmc.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("WebSocketOrderbook")
@Builder
public class WebSocketOrderbook implements Serializable {

    private String market;
    private String jsonData;


}
