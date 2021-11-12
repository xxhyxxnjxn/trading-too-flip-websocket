package com.gmc.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "flip_trading_api")
public class FlipTradingApi {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.AUTO)// 프로젝트에서 연결된 DB 의 넘버링 전략을 따라간다.
    @Column(nullable = false, name = "flip_trading_api_idx")
    private Long flipTradingApiIdx;
    @Column(nullable = false, name = "api_key")
    private String apiKey;
    @Column(nullable = false, name = "secret_key")
    private String secretKey;
    @Column(nullable = true, name = "pass_phrase")
    private String passPhrase;
    @ManyToOne(fetch = FetchType.EAGER)//연관관계맺음 Many = Many, User =One
    @JoinColumn(name = "id")
    private AuthUser authUser;
    @ManyToOne(fetch = FetchType.EAGER)//연관관계맺음 Many = Many, User =One
    @JoinColumn(name = "flip_exchange_idx")
    private FlipExchange flipExchange;

    @Builder
    public FlipTradingApi(String apiKey, String secretKey, String passPhrase, AuthUser authUser, FlipExchange flipExchange) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passPhrase = passPhrase;
        this.authUser = authUser;
        this.flipExchange = flipExchange;
    }
}
