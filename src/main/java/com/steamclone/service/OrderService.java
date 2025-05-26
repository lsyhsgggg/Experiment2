package com.steamclone.service;

import com.steamclone.dao.CartDAO;
import com.steamclone.dao.OrderDAO;
import com.steamclone.entity.Game;
import com.steamclone.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private CartService cartService;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.cartService = new CartService();
    }

    // 创建订单（结算购物车）
    public boolean checkout(int userId) {
        // 获取购物车中的所有游戏
        List<Game> cartItems = cartService.getAllCartItems(userId);

        if (cartItems.isEmpty()) {
            return false;
        }

        // 计算总金额
        BigDecimal totalAmount = cartService.getCartTotalAmount(userId);

        // 创建订单
        boolean success = orderDAO.createOrder(userId, cartItems, totalAmount);

        // 如果订单创建成功，清空购物车
        if (success) {
            cartDAO.clearCart(userId);
        }

        return success;
    }

    // 获取用户的所有订单
    public List<Order> getUserOrders(int userId) {
        return orderDAO.getUserOrders(userId);
    }

    // 获取订单详情
    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }
}