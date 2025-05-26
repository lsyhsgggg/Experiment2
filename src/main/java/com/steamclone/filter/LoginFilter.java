package com.steamclone.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = {"/user/profile", "/user/library", "/cart/*", "/order/*"})
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 过滤器初始化
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        // 检查用户是否已登录
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // 允许的公共资源
        String requestURI = request.getRequestURI();
        boolean isLoginPage = requestURI.contains("/user/login");
        boolean isRegisterPage = requestURI.contains("/user/register");

        if (isLoggedIn || isLoginPage || isRegisterPage) {
            // 用户已登录或正在访问登录/注册页面，继续请求
            filterChain.doFilter(request, response);
        } else {
            // 用户未登录，重定向到登录页面
            String targetUrl = request.getRequestURI();
            String queryString = request.getQueryString();

            if (queryString != null && !queryString.isEmpty()) {
                targetUrl += "?" + queryString;
            }

            response.sendRedirect(request.getContextPath() + "/user/login?targetUrl=" + targetUrl);
        }
    }

    @Override
    public void destroy() {
        // 过滤器销毁
    }
}