package com.gmc.websocket.repository;

import com.gmc.websocket.domain.FlipCurrency;
import com.gmc.websocket.domain.WebSocketOrderbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketOrderbookRepository extends JpaRepository<WebSocketOrderbook, Long> {
    WebSocketOrderbook findByFlipCurrency_FlipCurrencyNameAndFlipCurrency_FlipPaymentAndFlipCurrency_FlipExchange_FlipExchangeName(String currency, String payment, String exchange);

    WebSocketOrderbook findByFlipCurrency(FlipCurrency flipCurrency);
}
