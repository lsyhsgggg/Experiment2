package com.steamclone.service;

import com.steamclone.dao.GameDAO;
import com.steamclone.dto.UserGameDTO;
import com.steamclone.entity.Game;
import com.steamclone.util.PageUtil;

import java.util.List;

public class GameService {
    private GameDAO gameDAO;

    public GameService() {
        this.gameDAO = new GameDAO();
    }

    // 获取游戏列表（带分页）
    public List<Game> getGameList(int page, int pageSize) {
        return gameDAO.findAll(page, pageSize);
    }

    // 获取游戏总页数
    public int getGameTotalPages(int pageSize) {
        int total = gameDAO.getGameCount();
        return PageUtil.calculateTotalPages(total, pageSize);
    }

    // 获取游戏总数
    public int getGameCount() {
        return gameDAO.getGameCount();
    }

    // 获取游戏详情
    public Game getGameDetail(int gameId) {
        return gameDAO.findById(gameId);
    }

    // 获取特色游戏
    public List<Game> getFeaturedGames(int count) {
        return gameDAO.getFeaturedGames(count);
    }

    // 根据关键词搜索游戏
    public List<Game> searchGames(String keyword, int page, int pageSize) {
        return gameDAO.searchByKeyword(keyword, page, pageSize);
    }

    // 获取搜索结果总页数
    public int getSearchResultTotalPages(String keyword, int pageSize) {
        int total = gameDAO.getSearchResultCount(keyword);
        return PageUtil.calculateTotalPages(total, pageSize);
    }

    // 获取用户游戏库（带分页）
    // 修改返回类型
    public List<UserGameDTO> getUserLibrary(int userId, int page, int pageSize) {
        return gameDAO.findUserLibrary(userId, page, pageSize);
    }

    // 获取用户游戏库总页数
    public int getUserLibraryTotalPages(int userId, int pageSize) {
        int total = gameDAO.getUserLibraryCount(userId);
        return PageUtil.calculateTotalPages(total, pageSize);
    }

    // 获取用户游戏库总数
    public int getUserLibraryCount(int userId) {
        return gameDAO.getUserLibraryCount(userId);
    }

    // 检查用户是否拥有游戏
    public boolean userOwnsGame(int userId, int gameId) {
        return gameDAO.userOwnsGame(userId, gameId);
    }

}