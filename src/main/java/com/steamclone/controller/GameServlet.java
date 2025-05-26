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

@WebServlet(name = "GameServlet", urlPatterns = {"/game/*"})
public class GameServlet extends HttpServlet {
    private GameService gameService;
    private CartService cartService;

    @Override
    public void init() {
        gameService = new GameService();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            // 解析游戏ID
            int gameId = Integer.parseInt(pathInfo.substring(1));

            // 获取游戏详情
            Game game = gameService.getGameDetail(gameId);

            if (game == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "游戏不存在");
                return;
            }

            // 设置游戏详情属性
            request.setAttribute("game", game);

            // 获取用户登录状态
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                // 检查用户是否已拥有该游戏
                boolean ownsGame = gameService.userOwnsGame(user.getUserId(), gameId);
                request.setAttribute("ownsGame", ownsGame);

                // 检查游戏是否已在购物车中
                boolean inCart = cartService.isGameInCart(user.getUserId(), gameId);
                request.setAttribute("inCart", inCart);

                // 获取购物车数量
                int cartItemCount = cartService.getCartItemCount(user.getUserId());
                request.setAttribute("cartItemCount", cartItemCount);
            }

            // 转发到游戏详情页
            request.getRequestDispatcher("/game-details.jsp").forward(request, response);

        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的游戏ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}