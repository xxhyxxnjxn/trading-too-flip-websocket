package com.gmc.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DynamicUpdate
@Table(name = "flip_trading_setting")
public class FlipTradingSetting {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.AUTO)// 프로젝트에서 연결된 DB 의 넘버링 전략을 따라간다.
    @Column(nullable = false, name = "flip_trading_setting_idx")
    private Long flipTradingSettingIdx;
    @ManyToOne(fetch = FetchType.EAGER)//연관관계맺음 Many = Many, User =One
    @JoinColumn(name = "flip_trading_api_idx")
    private FlipTradingApi flipTradingApi;
    @Column(nullable = false, name = "flip_start_amount")
    private Double flipStartAmount;
    @Column(nullable = false, name = "flip_revenue")
    private Double flipRevenue;
    @Column(nullable = false, name = "flip_buy_count")
    private Double flipBuyCount;
    @Column(nullable = false, name = "flip_buy_total_percent")
    @ColumnDefault("10")
    private Double flipBuyTotalPercent;
    @Column(nullable = false, name = "stop_loss")
    private Double stopLoss;
    @Column(nullable = false, name = "flip_trading_check")
    private boolean flipTradingCheck;
    @Column(nullable = false, name = "stop_loss_check")
    private boolean stopLossCheck;
    @Column(nullable = false, name = "flip_reserve_closing")
    private boolean flipReserveClosing;
    @ManyToOne(fetch = FetchType.EAGER)//연관관계맺음 Many = Many, User =One
    @JoinColumn(name = "flip_currency_idx")
    private FlipCurrency flipCurrency;
    @Column(name = "flip_working_funds")
    private Double flipWorkingFunds;
    @Column(name = "flip_first_bid_price")
    private Double flipFirstBidPrice;
    @Column(name = "flip_avg_price")
    @ColumnDefault("0.0")
    private Double flipAvgPrice;

    @Builder
    public FlipTradingSetting(FlipTradingApi flipTradingApi, Double flipAvgPrice, Double flipBuyTotalPercent, Double flipStartAmount, Double flipRevenue, Double flipWorkingFunds, Double flipFirstBidPrice, Double flipBuyCount, Double stopLoss, boolean flipTradingCheck, boolean stopLossCheck, boolean flipReserveClosing, FlipCurrency flipCurrency) {
        this.flipTradingApi = flipTradingApi;
        this.flipAvgPrice = flipAvgPrice;
        this.flipBuyTotalPercent = flipBuyTotalPercent;
        this.flipStartAmount = flipStartAmount;
        this.flipRevenue = flipRevenue;
        this.flipBuyCount = flipBuyCount;
        this.stopLoss = stopLoss;
        this.flipTradingCheck = flipTradingCheck;
        this.stopLossCheck = stopLossCheck;
        this.flipReserveClosing = flipReserveClosing;
        this.flipCurrency = flipCurrency;
        this.flipWorkingFunds = flipWorkingFunds;
        this.flipFirstBidPrice = flipFirstBidPrice;
    }
}
