package com.steamclone.dto;

import com.steamclone.entity.Game;
import java.util.Date;

public class UserGameDTO {
    private Game game;
    private Date purchaseDate;

    public UserGameDTO(Game game, Date purchaseDate) {
        this.game = game;
        this.purchaseDate = purchaseDate;
    }

    // 为了在JSP中方便访问游戏属性，提供代理方法
    public int getGameId() {
        return game.getGameId();
    }

    public String getName() {
        return game.getName();
    }

    public String getImageUrl() {
        return game.getImageUrl();
    }

    // 其他游戏属性的代理方法...

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}