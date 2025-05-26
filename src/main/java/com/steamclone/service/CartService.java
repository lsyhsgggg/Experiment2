package com.steamclone.service;

import com.steamclone.dao.CartDAO;
import com.steamclone.dao.GameDAO;
import com.steamclone.entity.Game;
import com.steamclone.util.PageUtil;

import java.math.BigDecimal;
import java.util.List;

public class CartService {
    private CartDAO cartDAO;
    private GameDAO gameDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.gameDAO = new GameDAO();
    }

    // 添加游戏到购物车
    public boolean addToCart(int userId, int gameId) {
        // 检查用户是否已拥有该游戏
        if (gameDAO.userOwnsGame(userId, gameId)) {
            return false;
        }

        return cartDAO.addToCart(userId, gameId);
    }

    // 从购物车移除游戏
    public boolean removeFromCart(int userId, int gameId) {
        return cartDAO.removeFromCart(userId, gameId);
    }

    // 清空购物车
    public boolean clearCart(int userId) {
        return cartDAO.clearCart(userId);
    }

    // 获取购物车中的游戏（带分页）
    public List<Game> getCartItems(int userId, int page, int pageSize) {
        return cartDAO.getCartItems(userId, page, pageSize);
    }

    // 获取购物车中所有游戏（不分页）
    public List<Game> getAllCartItems(int userId) {
        return cartDAO.getAllCartItems(userId);
    }

    // 获取购物车总金额
    public BigDecimal getCartTotalAmount(int userId) {
        List<Game> cartItems = getAllCartItems(userId);
        BigDecimal total = BigDecimal.ZERO;

        for (Game game : cartItems) {
            total = total.add(game.getDiscountedPrice());
        }

        return total;
    }

    // 获取购物车总页数
    public int getCartTotalPages(int userId, int pageSize) {
        int total = cartDAO.getCartItemCount(userId);
        return PageUtil.calculateTotalPages(total, pageSize);
    }

    // 获取购物车中游戏总数
    public int getCartItemCount(int userId) {
        return cartDAO.getCartItemCount(userId);
    }

    // 检查游戏是否在购物车中
    public boolean isGameInCart(int userId, int gameId) {
        return cartDAO.isGameInCart(userId, gameId);
    }
}