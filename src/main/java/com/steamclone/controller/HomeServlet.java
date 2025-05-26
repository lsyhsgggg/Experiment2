package com.steamclone.controller;

import com.steamclone.entity.Game;
import com.steamclone.entity.User;
import com.steamclone.service.CartService;
import com.steamclone.service.GameService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {
    private GameService gameService;
    private CartService cartService;

    @Override
    public void init() {
        gameService = new GameService();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取分页参数
            int page = 1;
            int pageSize = 8;

            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) {
                        page = 1;
                    }
                }
            } catch (NumberFormatException e) {
                page = 1;
            }

            // 获取搜索参数
            String keyword = request.getParameter("search");

            List<Game> games;
            int totalPages;

            // 如果有搜索关键词，显示搜索结果
            if (keyword != null && !keyword.isEmpty()) {
                games = gameService.searchGames(keyword, page, pageSize);
                totalPages = gameService.getSearchResultTotalPages(keyword, pageSize);
                request.setAttribute("keyword", keyword);
            } else {
                // 否则显示所有游戏
                games = gameService.getGameList(page, pageSize);
                totalPages = gameService.getGameTotalPages(pageSize);
            }

            // 获取特色游戏(用于轮播图)
            List<Game> featuredGames = gameService.getFeaturedGames(3); // 获取3个特色游戏

            // 预处理游戏描述文本，避免在JSP中使用复杂的字符串操作
            for (Game game : featuredGames) {
                String desc = game.getDescription();
                if (desc != null && desc.length() > 120) {
                    game.setDescription(desc.substring(0, 120) + "...");
                }
            }

            // 设置属性
            request.setAttribute("games", games);
            request.setAttribute("featuredGames", featuredGames);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            // 获取用户登录状态
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            // 如果用户已登录，获取购物车数量
            if (user != null) {
                int cartItemCount = cartService.getCartItemCount(user.getUserId());
                request.setAttribute("cartItemCount", cartItemCount);
            }

            // 转发到首页
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            // 打印完整堆栈以便调试
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}