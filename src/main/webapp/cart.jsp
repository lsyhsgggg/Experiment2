<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购物车 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="cart-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="cart-container">
        <h1>购物车</h1>

        <!-- 错误消息显示 -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                    ${errorMessage}
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty cartItems}">
                <div class="empty-cart">
                    <p>您的购物车为空。</p>
                    <a href="${pageContext.request.contextPath}/" class="layui-btn">浏览商店</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="cart-items">
                    <c:forEach var="game" items="${cartItems}">
                        <div class="cart-item">
                            <div class="item-image">
                                <!-- 替换为游戏图片 -->
                                <img src="${pageContext.request.contextPath}/${game.imageUrl}" alt="${game.name}">
                            </div>
                            <div class="item-details">
                                <h3 class="item-title">${game.name}</h3>
                                <div class="item-publisher">${game.publisher}</div>
                            </div>
                            <div class="item-price">
                                <c:choose>
                                    <c:when test="${game.price.doubleValue() == 0}">
                                        <span class="free">免费畅玩</span>
                                    </c:when>
                                    <c:when test="${game.hasDiscount()}">
                                        <span class="discount">-${game.discountPercentage}%</span>
                                        <span class="original-price">¥${game.price}</span>
                                        <span class="current-price">¥${game.discountedPrice}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="current-price">¥${game.price}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="item-actions">
                                <button class="layui-btn layui-btn-danger layui-btn-sm remove-item" data-game-id="${game.gameId}">移除</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- 分页 -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <ul>
                            <c:if test="${currentPage > 1}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/cart/view?page=${currentPage - 1}">上一页</a>
                                </li>
                            </c:if>

                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <li class="${i == currentPage ? 'active' : ''}">
                                    <a href="${pageContext.request.contextPath}/cart/view?page=${i}">${i}</a>
                                </li>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/cart/view?page=${currentPage + 1}">下一页</a>
                                </li>
                            </c:if>
                        </ul>
                    </div>
                </c:if>

                <!-- 购物车摘要和结算 -->
                <div class="cart-summary">
                    <div class="summary-details">
                        <div class="summary-item">
                            <span class="summary-label">商品数量:</span>
                            <span class="summary-value" id="item-count">${itemCount}</span>
                        </div>
                        <div class="summary-item">
                            <span class="summary-label">商品总价:</span>
                            <span class="summary-value" id="total-amount">¥${totalAmount}</span>
                        </div>
                    </div>

                    <div class="summary-actions">
                        <a href="${pageContext.request.contextPath}/" class="layui-btn layui-btn-primary">继续购物</a>
                        <form action="${pageContext.request.contextPath}/cart/clear" method="post" style="display: inline-block;">
                            <button type="submit" class="layui-btn layui-btn-danger">清空购物车</button>
                        </form>
                        <a href="${pageContext.request.contextPath}/order/checkout" class="layui-btn">结算</a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
<script>
    layui.use(['layer', 'jquery'], function() {
        var layer = layui.layer;
        var $ = layui.jquery;

        // 移除购物车项
        $('.remove-item').on('click', function() {
            var gameId = $(this).data('game-id');
            var $item = $(this).closest('.cart-item');

            $.ajax({
                url: '${pageContext.request.contextPath}/cart/remove',
                method: 'POST',
                data: {gameId: gameId},
                success: function(res) {
                    if(res.success) {
                        layer.msg(res.message);

                        // 更新购物车数量
                        $('.cart-count').text(res.cartItemCount);
                        if(res.cartItemCount === 0) {
                            $('.cart-count').remove();
                        }

                        // 移除购物车项
                        $item.fadeOut(200, function() {
                            $(this).remove();

                            // 延迟0.7秒后刷新页面更新总价
                            setTimeout(function() {
                                location.reload();
                            }, 700);
                        });
                    } else {
                        layer.msg(res.message);
                    }
                },
                error: function() {
                    layer.msg('操作失败，请稍后再试');
                }
            });
        });
    });
</script>
</body>
</html>