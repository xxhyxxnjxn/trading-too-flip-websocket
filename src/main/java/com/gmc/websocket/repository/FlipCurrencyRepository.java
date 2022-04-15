package com.gmc.websocket.repository;

import com.gmc.websocket.domain.FlipCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlipCurrencyRepository extends JpaRepository<FlipCurrency, Long> {

    List<FlipCurrency> findByFlipExchange_FlipExchangeName(String exchangeName);

    List<FlipCurrency> findAll();

}
