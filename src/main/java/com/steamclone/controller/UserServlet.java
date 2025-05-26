package com.steamclone.controller;

import com.steamclone.dto.UserGameDTO;
import com.steamclone.entity.Game;
import com.steamclone.entity.User;
import com.steamclone.service.CartService;
import com.steamclone.service.GameService;
import com.steamclone.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// 修改这里，添加/user/library到URL映射列表
@WebServlet(name = "UserServlet", urlPatterns = {"/user/login", "/user/register", "/user/logout", "/user/profile", "/user/library"})
public class UserServlet extends HttpServlet {
    private UserService userService;
    private CartService cartService;

    // Cookie 持续时间 - 30天
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30;

    @Override
    public void init() {
        userService = new UserService();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        // 尝试通过Cookie自动登录
        autoLoginFromCookie(request);

        if (uri.contains("/user/login")) {
            // 如果用户已登录，直接跳转到首页
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }

            // 显示登录页面
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if (uri.contains("/user/register")) {
            // 显示注册页面
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else if (uri.contains("/user/logout")) {
            // 用户登出
            HttpSession session = request.getSession();
            session.invalidate();

            // 清除Cookie
            Cookie userIdCookie = new Cookie("userId", "");
            userIdCookie.setMaxAge(0);
            userIdCookie.setPath("/");
            response.addCookie(userIdCookie);

            Cookie usernameCookie = new Cookie("username", "");
            usernameCookie.setMaxAge(0);
            usernameCookie.setPath("/");
            response.addCookie(usernameCookie);

            response.sendRedirect(request.getContextPath() + "/");
        } else if (uri.contains("/user/profile")) {
            // 显示用户资料页面
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/user/login");
                return;
            }

            // 获取用户信息并设置到请求属性
            request.setAttribute("user", user);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }else if (uri.contains("/user/library")) {
            // 处理用户游戏库请求
            showUserLibrary(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (uri.contains("/user/login")) {
            // 处理登录请求
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            User user = userService.login(username, password);

            if (user != null) {
                // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // 获取购物车数量
                int cartItemCount = cartService.getCartItemCount(user.getUserId());
                session.setAttribute("cartItemCount", cartItemCount);

                // 创建持久登录Cookie
                Cookie userIdCookie = new Cookie("userId", String.valueOf(user.getUserId()));
                userIdCookie.setMaxAge(COOKIE_MAX_AGE);
                userIdCookie.setPath("/");
                response.addCookie(userIdCookie);

                Cookie usernameCookie = new Cookie("username", user.getUsername());
                usernameCookie.setMaxAge(COOKIE_MAX_AGE);
                usernameCookie.setPath("/");
                response.addCookie(usernameCookie);

                // 检查是否有重定向URL（从游戏详情页面跳转来的）
                String redirectBack = request.getParameter("redirectBack");
                if (redirectBack != null && !redirectBack.isEmpty()) {
                    response.sendRedirect(redirectBack);
                    return;
                }

                // 没有指定重定向URL，返回首页
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                // 登录失败
                request.setAttribute("error", "用户名或密码错误");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } else if (uri.contains("/user/register")) {
            // 处理注册请求
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            boolean success = userService.register(username, password, email);

            if (success) {
                // 注册成功，重定向到登录页面
                request.setAttribute("message", "注册成功，请登录");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                // 注册失败
                request.setAttribute("error", "注册失败，用户名可能已存在");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        }
    }

    // 尝试从Cookie自动登录
    private void autoLoginFromCookie(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        // 如果已登录，不需要检查Cookie
        if (session != null && session.getAttribute("user") != null) {
            return;
        }

        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String userId = null;
                String username = null;

                for (Cookie cookie : cookies) {
                    if ("userId".equals(cookie.getName())) {
                        userId = cookie.getValue();
                    } else if ("username".equals(cookie.getName())) {
                        username = cookie.getValue();
                    }
                }

                // 如果找到Cookie，尝试自动登录
                if (userId != null && username != null) {
                    User user = userService.getUserById(Integer.parseInt(userId));

                    // 验证用户名匹配
                    if (user != null && user.getUsername().equals(username)) {
                        // 创建会话
                        session = request.getSession(true);
                        session.setAttribute("user", user);

                        // 加载购物车计数
                        int cartItemCount = cartService.getCartItemCount(user.getUserId());
                        session.setAttribute("cartItemCount", cartItemCount);
                    }
                }
            }
        } catch (Exception e) {
            // 忽略任何错误，让用户重新登录
            e.printStackTrace();
        }
    }

    // 显示用户游戏库
    private void showUserLibrary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取当前登录用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // 用户未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        // 输出调试日志
        System.out.println("正在获取用户ID: " + user.getUserId() + " 的游戏库");

        // 获取用户的游戏库
        GameService gameService = new GameService();

        // 默认第一页，每页10条
        int page = 1;
        int pageSize = 10;

        // 如果URL中有页码参数，则使用URL中的页码
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                // 忽略无效页码
            }
        }

        // 调用修改后的方法获取用户游戏DTO列表
        List<UserGameDTO> userGames = gameService.getUserLibrary(user.getUserId(), page, pageSize);

        // 输出调试信息
        System.out.println("找到用户游戏数量: " + userGames.size());

        // 获取总页数
        int totalPages = gameService.getUserLibraryTotalPages(user.getUserId(), pageSize);

        // 设置属性
        request.setAttribute("userGames", userGames);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // 获取购物车数量
        CartService cartService = new CartService();
        int cartItemCount = cartService.getCartItemCount(user.getUserId());
        request.setAttribute("cartItemCount", cartItemCount);

        // 转发到用户游戏库页面
        request.getRequestDispatcher("/user-library.jsp").forward(request, response);
    }
}