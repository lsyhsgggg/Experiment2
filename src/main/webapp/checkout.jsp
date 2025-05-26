<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>结算 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="checkout-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="checkout-container">
        <h1>结算</h1>

        <!-- 错误消息显示 -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                    ${errorMessage}
            </div>
        </c:if>

        <div class="checkout-summary">
            <h2>订单摘要</h2>

            <div class="checkout-items">
                <c:forEach var="game" items="${cartItems}">
                    <div class="checkout-item">
                        <div class="item-image">
                            <!-- 替换为游戏图片 -->
                            <img src="${pageContext.request.contextPath}/${game.imageUrl}" alt="${game.name}">
                        </div>
                        <div class="item-details">
                            <h3 class="item-title">${game.name}</h3>
                        </div>
                        <div class="item-price">
                            <c:choose>
                                <c:when test="${game.price.doubleValue() == 0}">
                                    <span class="free">免费畅玩</span>
                                </c:when>
                                <c:when test="${game.hasDiscount()}">
                                    <span class="discount">-${game.discountPercentage}%</span>
                                    <span class="current-price">¥${game.discountedPrice}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="current-price">¥${game.price}</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="checkout-total">
                <div class="total-item">
                    <span class="total-label">商品数量:</span>
                    <span class="total-value">${itemCount}</span>
                </div>
                <div class="total-item">
                    <span class="total-label">订单总价:</span>
                    <span class="total-value price">¥${totalAmount}</span>
                </div>
            </div>
        </div>

        <!-- 支付信息 - 在实际项目中需要集成真正的支付系统 -->
        <div class="payment-section">
            <h2>支付方式</h2>

            <form class="layui-form" action="${pageContext.request.contextPath}/order/place" method="post" lay-filter="checkout-form">
                <div class="layui-form-item">
                    <label class="layui-form-label">支付方式</label>
                    <div class="layui-input-block">
                        <input type="radio" name="paymentMethod" value="credit" title="信用卡" checked>
                        <input type="radio" name="paymentMethod" value="alipay" title="支付宝">
                        <input type="radio" name="paymentMethod" value="wechat" title="微信支付">
                    </div>
                </div>

                <div class="layui-form-item payment-disclaimer">
                    <p>注意：这是一个学习项目，不会实际扣款。点击"完成购买"后，游戏将直接添加到您的库中。</p>
                </div>

                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" lay-submit lay-filter="checkout-submit">完成购买</button>
                        <a href="${pageContext.request.contextPath}/cart/view" class="layui-btn layui-btn-primary">返回购物车</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
<script>
    layui.use(['form', 'layer'], function() {
        var form = layui.form;
        var layer = layui.layer;

        // 提交订单时显示加载动画
        form.on('submit(checkout-submit)', function() {
            layer.load(1, {
                shade: [0.5, '#000']
            });
            return true;
        });
    });
</script>
</body>
</html>