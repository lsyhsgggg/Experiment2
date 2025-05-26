package com.steamclone.dao;

import com.steamclone.entity.Game;
import com.steamclone.entity.Order;
import com.steamclone.entity.OrderDetail;
import com.steamclone.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // 创建订单并添加所有购物车商品
    public boolean createOrder(int userId, List<Game> games, BigDecimal totalAmount) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement detailStmt = null;
        PreparedStatement libraryStmt = null;
        PreparedStatement cartStmt = null; // 添加清空购物车的语句
        ResultSet generatedKeys = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);  // 开始事务

            // 创建订单 - 修复问题：添加所有必要字段
            String orderSql = "INSERT INTO orders (user_id, total_amount, order_date, status) VALUES (?, ?, NOW(), 'completed')";
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setBigDecimal(2, totalAmount);

            // 添加调试信息
            System.out.println("执行订单插入SQL: " + orderSql);
            System.out.println("参数: userId=" + userId + ", totalAmount=" + totalAmount);

            int orderRows = orderStmt.executeUpdate();
            System.out.println("订单插入结果: 影响 " + orderRows + " 行");

            // 获取生成的订单ID
            generatedKeys = orderStmt.getGeneratedKeys();
            int orderId;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
                System.out.println("订单成功创建! 订单ID: " + orderId);

                // 添加订单明细
                String detailSql = "INSERT INTO order_detail (order_id, game_id, price) VALUES (?, ?, ?)";
                detailStmt = conn.prepareStatement(detailSql);

                // 添加游戏到用户游戏库
                String librarySql = "INSERT INTO user_library (user_id, game_id, purchase_date) VALUES (?, ?, NOW())";
                libraryStmt = conn.prepareStatement(librarySql);

                for (Game game : games) {
                    // 添加订单明细
                    detailStmt.setInt(1, orderId);
                    detailStmt.setInt(2, game.getGameId());

                    // 正确处理折扣价格
                    BigDecimal price;
                    if (game.getDiscountedPrice() != null && game.getDiscountedPrice().compareTo(BigDecimal.ZERO) > 0) {
                        price = game.getDiscountedPrice();
                    } else {
                        price = game.getPrice();
                    }

                    detailStmt.setBigDecimal(3, price);
                    int detailRows = detailStmt.executeUpdate();
                    System.out.println("添加订单明细: 订单ID=" + orderId + ", 游戏ID=" + game.getGameId() + ", 价格=" + price + ", 影响行数=" + detailRows);

                    // 添加到用户游戏库（如果不存在）
                    libraryStmt.setInt(1, userId);
                    libraryStmt.setInt(2, game.getGameId());
                    try {
                        int libraryRows = libraryStmt.executeUpdate();
                        System.out.println("添加游戏到用户库: 用户ID=" + userId + ", 游戏ID=" + game.getGameId() + ", 影响行数=" + libraryRows);
                    } catch (SQLException e) {
                        // 忽略唯一键冲突（如果游戏已在库中）
                        if (e.getErrorCode() != 1062) {
                            throw e;
                        } else {
                            System.out.println("游戏已在用户库中: 用户ID=" + userId + ", 游戏ID=" + game.getGameId());
                        }
                    }
                }

                // 清空购物车 - 添加这一步确保购物车被清空
                String cartSql = "DELETE FROM shopping_cart WHERE user_id = ?";
                cartStmt = conn.prepareStatement(cartSql);
                cartStmt.setInt(1, userId);
                int cartRows = cartStmt.executeUpdate();
                System.out.println("清空用户购物车: 用户ID=" + userId + ", 影响行数=" + cartRows);

                // 提交事务
                conn.commit();
                success = true;
                System.out.println("事务提交成功! 订单处理完成!");
            } else {
                System.err.println("创建订单失败: 无法获取订单ID");
                conn.rollback();
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();  // 发生异常则回滚事务
                    System.err.println("订单处理失败，事务回滚: " + e.getMessage());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);  // 恢复自动提交
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBUtil.close(null, cartStmt, null);
            DBUtil.close(null, libraryStmt, null);
            DBUtil.close(null, detailStmt, null);
            DBUtil.close(null, orderStmt, generatedKeys);
            DBUtil.close(conn, null, null);
        }

        return success;
    }

    // 获取用户的所有订单
    public List<Order> getUserOrders(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));

                // 获取订单明细
                order.setOrderDetails(getOrderDetails(order.getOrderId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return orders;
    }

    // 获取订单明细
    private List<OrderDetail> getOrderDetails(int orderId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OrderDetail> details = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT od.*, g.* FROM order_detail od " +
                    "JOIN game g ON od.game_id = g.game_id " +
                    "WHERE od.order_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setDetailId(rs.getInt("detail_id"));
                detail.setOrderId(rs.getInt("order_id"));
                detail.setGameId(rs.getInt("game_id"));
                detail.setPrice(rs.getBigDecimal("price"));

                // 设置关联的游戏信息
                Game game = new Game();
                game.setGameId(rs.getInt("game_id"));
                game.setName(rs.getString("name"));
                game.setDescription(rs.getString("description"));
                game.setPrice(rs.getBigDecimal("price"));
                game.setReleaseDate(rs.getDate("release_date"));
                game.setDeveloper(rs.getString("developer"));
                game.setPublisher(rs.getString("publisher"));
                game.setImageUrl(rs.getString("image_url"));
                game.setDiscountRate(rs.getBigDecimal("discount_rate"));
                detail.setGame(game);

                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return details;
    }

    // 获取订单详情
    public Order getOrderById(int orderId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Order order = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM orders WHERE order_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));

                // 获取订单明细
                order.setOrderDetails(getOrderDetails(order.getOrderId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return order;
    }
}