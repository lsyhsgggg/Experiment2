package com.steamclone.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Game {
    private int gameId;
    private String name;
    private String description;
    private BigDecimal price;
    private Date releaseDate;
    private String developer;
    private String publisher;
    private String imageUrl;
    private BigDecimal discountRate;

    // 构造方法
    public Game() {}

    public Game(int gameId, String name, String description, BigDecimal price,
                Date releaseDate, String developer, String publisher,
                String imageUrl, BigDecimal discountRate) {
        this.gameId = gameId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
        this.discountRate = discountRate;
    }

    // Getters and Setters
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    // 获取折扣后价格
    public BigDecimal getDiscountedPrice() {
        return price.multiply(discountRate);
    }

    // 判断是否有折扣
    public boolean hasDiscount() {
        return discountRate != null && discountRate.compareTo(BigDecimal.ONE) < 0;
    }

    // 获取折扣百分比 - 修改方法名为标准的JavaBean规范
    public int getDiscountPercentage() {
        if (!hasDiscount()) {
            return 0;
        }

        BigDecimal percentage = BigDecimal.ONE.subtract(discountRate).multiply(new BigDecimal(100));
        return percentage.intValue();
    }
}