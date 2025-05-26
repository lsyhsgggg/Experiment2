package com.steamclone.entity;

import java.math.BigDecimal;

public class OrderDetail {
    private int detailId;
    private int orderId;
    private int gameId;
    private BigDecimal price;
    private Game game;  // 关联的游戏信息

    // 构造方法
    public OrderDetail() {}

    public OrderDetail(int detailId, int orderId, int gameId, BigDecimal price) {
        this.detailId = detailId;
        this.orderId = orderId;
        this.gameId = gameId;
        this.price = price;
    }

    // Getters and Setters
    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}