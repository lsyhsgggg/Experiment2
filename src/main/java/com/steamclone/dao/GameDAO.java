package com.steamclone.dao;

import com.steamclone.dto.UserGameDTO;
import com.steamclone.entity.Game;
import com.steamclone.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    // 查询所有游戏 (带分页)
    public List<Game> findAll(int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM game LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            rs = stmt.executeQuery();

            while (rs.next()) {
                games.add(extractGameFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return games;
    }

    // 获取游戏总数
    public int getGameCount() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM game";
            stmt = conn.prepareStatement(sql);
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

    // 根据ID查询游戏
    public Game findById(int gameId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Game game = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM game WHERE game_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                game = extractGameFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return game;
    }

    // 根据关键词搜索游戏
    public List<Game> searchByKeyword(String keyword, int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM game WHERE name LIKE ? OR description LIKE ? LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            String likeParam = "%" + keyword + "%";
            stmt.setString(1, likeParam);
            stmt.setString(2, likeParam);
            stmt.setInt(3, pageSize);
            stmt.setInt(4, (page - 1) * pageSize);
            rs = stmt.executeQuery();

            while (rs.next()) {
                games.add(extractGameFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return games;
    }

    // 获取搜索结果总数
    public int getSearchResultCount(String keyword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM game WHERE name LIKE ? OR description LIKE ?";
            stmt = conn.prepareStatement(sql);
            String likeParam = "%" + keyword + "%";
            stmt.setString(1, likeParam);
            stmt.setString(2, likeParam);
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

    // 获取特色游戏
    public List<Game> getFeaturedGames(int count) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            // 可以根据需要调整特色游戏的选择条件
            String sql = "SELECT * FROM game ORDER BY release_date DESC LIMIT ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, count);
            rs = stmt.executeQuery();

            while (rs.next()) {
                games.add(extractGameFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return games;
    }

    // 获取用户的游戏库（带分页）
    public List<UserGameDTO> findUserLibrary(int userId, int page, int pageSize) {
        List<UserGameDTO> userGames = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql = "SELECT g.*, ug.purchase_date FROM game g " +
                    "JOIN user_library ug ON g.game_id = ug.game_id " +
                    "WHERE ug.user_id = ? " +
                    "ORDER BY ug.purchase_date DESC " +
                    "LIMIT ? OFFSET ?";

            int offset = (page - 1) * pageSize;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, offset);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Game game = new Game();
                game.setGameId(rs.getInt("game_id"));
                game.setName(rs.getString("name"));
                game.setPrice(rs.getBigDecimal("price"));
                game.setDescription(rs.getString("description"));
                game.setImageUrl(rs.getString("image_url"));
                game.setDeveloper(rs.getString("developer"));
                game.setPublisher(rs.getString("publisher"));
                game.setReleaseDate(rs.getDate("release_date"));
                // 其他游戏属性...

                java.sql.Timestamp purchaseDate = rs.getTimestamp("purchase_date");
                UserGameDTO userGame = new UserGameDTO(game, purchaseDate);
                userGames.add(userGame);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return userGames;
    }

    // 相应地修改统计方法
    public int getUserLibraryCount(int userId) {
        int count = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM user_library WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return count;
    }

    // 检查用户是否拥有游戏
    public boolean userOwnsGame(int userId, int gameId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean owns = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM user_library WHERE user_id = ? AND game_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                owns = rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return owns;
    }

    // 从ResultSet提取Game对象
    private Game extractGameFromResultSet(ResultSet rs) throws SQLException {
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
        return game;
    }
}