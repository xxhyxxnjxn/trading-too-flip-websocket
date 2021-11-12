package com.gmc.websocket.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "websocket_orderbook")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class WebSocketOrderbook {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "websocket_orderbook_idx")
    private long websocketOrderbookIdx;

    @ManyToOne(fetch = FetchType.EAGER)//연관관계맺음 Many = Many, User =One
    @JoinColumn(name = "flip_currency_idx")
    private FlipCurrency flipCurrency;

    @Type(type = "jsonb")
    @Column(nullable = false, columnDefinition = "jsonb", name = "json_data")
    private String jsonData;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}
