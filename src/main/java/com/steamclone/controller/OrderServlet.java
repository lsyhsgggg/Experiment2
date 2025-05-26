package com.steamclone.controller;

import com.steamclone.entity.Order;
import com.steamclone.entity.User;
import com.steamclone.entity.Game;
import com.steamclone.service.CartService;
import com.steamclone.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = {"/order/*"})
public class OrderServlet extends HttpServlet {
    private OrderService orderService;
    private CartService cartService;

    @Override
    public void init() {
        orderService = new OrderService();
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
            response.sendRedirect(request.getContextPath() + "/user/login?targetUrl=" + request.getContextPath() + "/order/history");
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/order/checkout");
        } else if (pathInfo.equals("/checkout")) {
            // 显示结账页面
            showCheckout(request, response, user);
        } else if (pathInfo.equals("/history")) {
            // 显示订单历史
            showOrderHistory(request, response, user);
        } else if (pathInfo.equals("/success")) {
            // 显示订单成功页面
            showOrderSuccess(request, response, user);
        } else if (pathInfo.startsWith("/detail/")) {
            // 显示订单详情
            showOrderDetail(request, response, user, pathInfo);
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
            // 未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else if (pathInfo.equals("/place")) {
            // 提交订单
            placeOrder(request, response, user);
        } else if (pathInfo.equals("/complete")) {
            // 添加对/complete路径的支持，直接调用placeOrder方法
            placeOrder(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // 显示结账页面
    private void showCheckout(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        // 获取购物车商品和总价
        int itemCount = cartService.getCartItemCount(user.getUserId());

        if (itemCount == 0) {
            // 购物车为空，重定向到购物车页面
            request.setAttribute("errorMessage", "购物车为空，无法结账");
            response.sendRedirect(request.getContextPath() + "/cart/view");
            return;
        }

        List<Game> cartItems = cartService.getAllCartItems(user.getUserId());
        BigDecimal totalAmount = cartService.getCartTotalAmount(user.getUserId());

        // 设置属性
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("itemCount", itemCount);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("cartItemCount", itemCount);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    // 提交订单
    private void placeOrder(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        // 创建订单（结算购物车）
        boolean success = orderService.checkout(user.getUserId());

        if (success) {
            // 订单创建成功，使用重定向而不是转发，避免刷新页面时重复提交
            response.sendRedirect(request.getContextPath() + "/order/success");
        } else {
            // 订单创建失败
            request.setAttribute("errorMessage", "订单处理失败，请稍后再试");
            showCheckout(request, response, user);
        }
    }

    // 显示订单成功页面
    private void showOrderSuccess(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        // 获取购物车数量（应该已经是0了，因为刚刚结算）
        int cartItemCount = cartService.getCartItemCount(user.getUserId());
        request.setAttribute("cartItemCount", cartItemCount);

        // 显示成功页面
        request.getRequestDispatcher("/order-success.jsp").forward(request, response);
    }

    // 显示订单历史
    private void showOrderHistory(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        List<Order> orders = orderService.getUserOrders(user.getUserId());

        // 设置属性
        request.setAttribute("orders", orders);

        // 获取购物车数量
        int cartItemCount = cartService.getCartItemCount(user.getUserId());
        request.setAttribute("cartItemCount", cartItemCount);

        request.getRequestDispatcher("/order-history.jsp").forward(request, response);
    }

    // 显示订单详情
    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response, User user, String pathInfo) throws ServletException, IOException {
        try {
            // 解析订单ID
            int orderId = Integer.parseInt(pathInfo.substring("/detail/".length()));

            // 获取订单详情
            Order order = orderService.getOrderById(orderId);

            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
                return;
            }

            // 检查订单是否属于当前登录用户
            if (order.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "您无权查看此订单");
                return;
            }

            // 设置属性
            request.setAttribute("order", order);

            // 获取购物车数量
            int cartItemCount = cartService.getCartItemCount(user.getUserId());
            request.setAttribute("cartItemCount", cartItemCount);

            request.getRequestDispatcher("/order-detail.jsp").forward(request, response);

        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的订单ID");
        }
    }
}