package com.gmc.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "flip_currency")
public class FlipCurrency {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "flip_currency_idx")
    private Long flipCurrencyIdx;
    @Column(nullable = false, name = "flip_currency_name")
    private String flipCurrencyName;
    @Column(nullable = true, name = "flip_currency_price_unit")
    private Double flipCurrencyPriceUnit;
    @Column(nullable = true, name = "flip_currency_quantity_unit")
    private Double flipCurrencyQuantityUnit;
    @Column(nullable = true, name = "flip_currency_min_quantity")
    private Double flipCurrencyMinQuantity;
    @Column(nullable = true, name = "flip_payment")
    private String flipPayment;
    @ManyToOne(fetch = FetchType.EAGER)//연관관계맺음 Many = Many, User =One
    @JoinColumn(name = "flip_exchange_idx")
    private FlipExchange flipExchange;
}
