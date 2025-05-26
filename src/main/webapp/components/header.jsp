<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="steam-header">
    <div class="global-header">
        <div class="header-content">
            <div class="header-logo">
                <a href="${pageContext.request.contextPath}/">
                    <img src="${pageContext.request.contextPath}/images/steam-logo.png" alt="Steam">
                </a>
            </div>

            <div class="header-navigation">
                <a href="${pageContext.request.contextPath}/" class="menu-item">商店</a>
                <a href="#" class="menu-item">社区</a>
                <a href="#" class="menu-item">关于</a>
                <a href="#" class="menu-item">客服</a>
            </div>

            <div class="header-actions">
                <div class="search-container">
                    <form action="${pageContext.request.contextPath}/home" method="get">
                        <input type="text" name="search" class="search-box" placeholder="搜索游戏..." value="${keyword}">
                        <button type="submit" class="search-btn">
                            <i class="layui-icon layui-icon-search"></i>
                        </button>
                    </form>
                </div>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div class="user-dropdown">
                            <div class="user-avatar">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user.avatarUrl}">
                                        <img src="${pageContext.request.contextPath}/${sessionScope.user.avatarUrl}" alt="Avatar">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/images/avatars/default_avatar.jpg" alt="Default Avatar">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <span class="username">${sessionScope.user.username}</span>
                            <div class="dropdown-content">
                                <a href="${pageContext.request.contextPath}/user-profile.jsp">我的账户</a>
                                <a href="${pageContext.request.contextPath}/user/library">我的游戏库</a>
                                <a href="${pageContext.request.contextPath}/user/logout">登出</a>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/cart/view" class="cart-btn">
                            <i class="layui-icon layui-icon-cart"></i>
                            <c:if test="${not empty cartItemCount && cartItemCount > 0}">
                                <span class="cart-count">${cartItemCount}</span>
                            </c:if>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/user/login" class="login-btn">登录</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <div class="store-nav">
        <div class="store-nav-inner">
            <a href="${pageContext.request.contextPath}/" class="tab">首页</a>
            <a href="#" class="tab">探索队列</a>
            <a href="#" class="tab">愿望单</a>
            <a href="#" class="tab">新品与特惠</a>
            <a href="#" class="tab">分类</a>
            <a href="#" class="tab">点数商店</a>
            <a href="#" class="tab">新闻</a>
            <a href="#" class="tab">实验室</a>
        </div>
    </div>
</header>