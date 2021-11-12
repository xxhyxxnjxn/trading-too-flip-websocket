package com.gmc.websocket.repository;

import com.gmc.websocket.domain.FlipCurrency;
import com.gmc.websocket.domain.WebSocketTicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketTickerRepository extends JpaRepository<WebSocketTicker, Long> {
    WebSocketTicker findByFlipCurrency(FlipCurrency findCurrency);

    WebSocketTicker findByFlipCurrencyAndFlipCurrency_FlipPaymentAndFlipCurrency_FlipExchange_FlipExchangeName(String currency, String payment, String site);
}
