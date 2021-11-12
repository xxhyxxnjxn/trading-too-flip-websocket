package com.gmc.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "flip_exchange")
public class FlipExchange {
    @Id // primary key
    @Column(nullable = false, name = "flip_exchange_idx")
    private Long flipExchangeIdx;
    @Column(nullable = false, name = "flip_exchange_name")
    private String flipExchangeName;
    @Column(nullable = true, name = "flip_exchange_min_amount")
    private Double flipExchangeMinAmount;
    @Column(nullable = true, name = "flip_exchange_kor_name")
    private String flipExchangeKorName;
    @Column(nullable = true, name = "flip_exchange_fee")
    private String flipExchangeFee;
}
