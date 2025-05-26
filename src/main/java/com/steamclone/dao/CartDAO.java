package com.steamclone.dao;

import com.steamclone.entity.Game;
import com.steamclone.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // 添加游戏到购物车
    public boolean addToCart(int userId, int gameId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO shopping_cart (user_id, game_id) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);

            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            // 可能是唯一键约束冲突，已经在购物车中
            if (e.getErrorCode() == 1062) { // MySQL的重复键错误码
                success = true;
            } else {
                e.printStackTrace();
            }
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return success;
    }

    // 从购物车删除游戏
    public boolean removeFromCart(int userId, int gameId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM shopping_cart WHERE user_id = ? AND game_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);

            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return success;
    }

    // 清空购物车
    public boolean clearCart(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            stmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return success;
    }

    // 获取用户购物车中的所有游戏（带分页）
    public List<Game> getCartItems(int userId, int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT g.* FROM game g " +
                    "JOIN shopping_cart sc ON g.game_id = sc.game_id " +
                    "WHERE sc.user_id = ? " +
                    "LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            rs = stmt.executeQuery();

            while (rs.next()) {
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
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return games;
    }

    // 获取购物车中所有游戏（不分页）
    public List<Game> getAllCartItems(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT g.* FROM game g " +
                    "JOIN shopping_cart sc ON g.game_id = sc.game_id " +
                    "WHERE sc.user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
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
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return games;
    }

    // 获取购物车中游戏总数
    public int getCartItemCount(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM shopping_cart WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return count;
    }

    // 检查游戏是否在购物车中
    public boolean isGameInCart(int userId, int gameId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean inCart = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM shopping_cart WHERE user_id = ? AND game_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                inCart = rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return inCart;
    }
}