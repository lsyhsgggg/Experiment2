<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单历史 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="orders-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="orders-container">
        <h1>订单历史</h1>

        <!-- 成功消息显示 -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                    ${successMessage}
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="empty-orders">
                    <p>您还没有订单记录。</p>
                    <a href="${pageContext.request.contextPath}/" class="layui-btn">开始购物</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="orders-list">
                    <table class="layui-table">
                        <thead>
                        <tr>
                            <th>订单号</th>
                            <th>日期</th>
                            <th>游戏数量</th>
                            <th>总金额</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td>${order.orderId}</td>
                                <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                <td>${order.orderDetails.size()}</td>
                                <td>¥${order.totalAmount}</td>
                                <td>
                                    <span class="order-status ${order.status}">${order.status}</span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/order/detail/${order.orderId}" class="layui-btn layui-btn-xs">查看详情</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
</body>
</html>