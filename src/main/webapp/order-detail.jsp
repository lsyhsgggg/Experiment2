<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单详情 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="order-detail-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="order-detail-container">
        <h1>订单详情</h1>

        <div class="order-info">
            <div class="order-header">
                <div class="order-id">订单号: ${order.orderId}</div>
                <div class="order-date">日期: <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
                <div class="order-status">状态: <span class="${order.status}">${order.status}</span></div>
            </div>

            <div class="order-items">
                <h2>购买的游戏</h2>

                <table class="layui-table">
                    <thead>
                    <tr>
                        <th>游戏</th>
                        <th>价格</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="detail" items="${order.orderDetails}">
                        <tr>
                            <td>
                                <div class="game-item">
                                    <div class="game-image">
                                        <!-- 替换为游戏图片 -->
                                        <img src="${pageContext.request.contextPath}/${detail.game.imageUrl}" alt="${detail.game.name}">
                                    </div>
                                    <div class="game-info">
                                        <div class="game-name">${detail.game.name}</div>
                                        <div class="game-publisher">${detail.game.publisher}</div>
                                    </div>
                                </div>
                            </td>
                            <td>¥${detail.price}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/game/${detail.game.gameId}" class="layui-btn layui-btn-xs">查看详情</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="order-summary">
                <div class="summary-item">
                    <span class="summary-label">游戏数量:</span>
                    <span class="summary-value">${order.orderDetails.size()}</span>
                </div>
                <div class="summary-item total">
                    <span class="summary-label">总金额:</span>
                    <span class="summary-value">¥${order.totalAmount}</span>
                </div>
            </div>

            <div class="order-actions">
                <a href="${pageContext.request.contextPath}/order/history" class="layui-btn layui-btn-primary">返回订单列表</a>
            </div>
        </div>
    </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
</body>
</html>