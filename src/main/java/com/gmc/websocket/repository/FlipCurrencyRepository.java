package com.gmc.websocket.repository;

import com.gmc.websocket.domain.FlipCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlipCurrencyRepository extends JpaRepository<FlipCurrency, Long> {
    FlipCurrency findFlipCurrencyByFlipExchange_FlipExchangeIdxAndAndFlipCurrencyName(Long exchange_idx, String currency_name);

    FlipCurrency findByFlipExchange_FlipExchangeNameAndFlipCurrencyName(String exchangeName, String currencyName);

    List<FlipCurrency> findByFlipExchange_FlipExchangeName(String exchangeName);

    List<FlipCurrency> findByFlipExchange_FlipExchangeNameOrderByFlipCurrencyName(String exchangeName);

    List<FlipCurrency> findAll();

}
