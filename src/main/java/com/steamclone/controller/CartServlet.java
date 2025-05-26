package com.steamclone.controller;

import com.steamclone.entity.Game;
import com.steamclone.entity.User;
import com.steamclone.service.CartService;
import com.steamclone.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart/*"})
public class CartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() {
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // 检查用户是否登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // 未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/user/login?targetUrl=" + request.getContextPath() + "/cart/view");
            return;
        }

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/view")) {
            // 查看购物车
            showCart(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // 检查用户是否登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // 用于AJAX请求的未登录响应
            if (pathInfo != null && (pathInfo.equals("/add") || pathInfo.equals("/remove"))) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "请先登录");
                JsonUtil.writeJsonResponse(response, result);
            } else {
                // 非AJAX请求重定向到登录页面
                response.sendRedirect(request.getContextPath() + "/user/login");
            }
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else if (pathInfo.equals("/add")) {
            // 添加游戏到购物车
            addToCart(request, response, user);
        } else if (pathInfo.equals("/remove")) {
            // 从购物车移除游戏
            removeFromCart(request, response, user);
        } else if (pathInfo.equals("/clear")) {
            // 清空购物车
            clearCart(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // 查看购物车
    private void showCart(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        // 获取分页参数
        int page = 1;
        int pageSize = 5;

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

        // 获取购物车商品
        List<Game> cartItems = cartService.getCartItems(user.getUserId(), page, pageSize);
        int totalPages = cartService.getCartTotalPages(user.getUserId(), pageSize);
        int itemCount = cartService.getCartItemCount(user.getUserId());
        BigDecimal totalAmount = cartService.getCartTotalAmount(user.getUserId());

        // 设置属性
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("itemCount", itemCount);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("cartItemCount", itemCount);

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    // 添加游戏到购物车
    private void addToCart(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String gameIdParam = request.getParameter("gameId");
        Map<String, Object> result = new HashMap<>();

        if (gameIdParam == null || gameIdParam.isEmpty()) {
            result.put("success", false);
            result.put("message", "无效的游戏ID");
            JsonUtil.writeJsonResponse(response, result);
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdParam);
            boolean success = cartService.addToCart(user.getUserId(), gameId);

            if (success) {
                int cartItemCount = cartService.getCartItemCount(user.getUserId());
                result.put("success", true);
                result.put("message", "已添加到购物车");
                result.put("cartItemCount", cartItemCount);
            } else {
                result.put("success", false);
                result.put("message", "您已拥有该游戏，无法添加到购物车");
            }
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "无效的游戏ID");
        }

        JsonUtil.writeJsonResponse(response, result);
    }

    // 从购物车移除游戏
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String gameIdParam = request.getParameter("gameId");
        Map<String, Object> result = new HashMap<>();

        if (gameIdParam == null || gameIdParam.isEmpty()) {
            result.put("success", false);
            result.put("message", "无效的游戏ID");
            JsonUtil.writeJsonResponse(response, result);
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdParam);
            boolean success = cartService.removeFromCart(user.getUserId(), gameId);

            if (success) {
                int cartItemCount = cartService.getCartItemCount(user.getUserId());
                result.put("success", true);
                result.put("message", "已从购物车移除");
                result.put("cartItemCount", cartItemCount);
            } else {
                result.put("success", false);
                result.put("message", "移除失败，该游戏可能不在购物车中");
            }
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "无效的游戏ID");
        }

        JsonUtil.writeJsonResponse(response, result);
    }

    // 清空购物车
    private void clearCart(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        boolean success = cartService.clearCart(user.getUserId());

        if (success) {
            response.sendRedirect(request.getContextPath() + "/cart/view");
        } else {
            request.setAttribute("errorMessage", "清空购物车失败，请稍后再试");
            showCart(request, response, user);
        }
    }
}