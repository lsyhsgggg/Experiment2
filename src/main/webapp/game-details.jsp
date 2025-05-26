<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${game.name} - Steam Clone</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="game-details-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
  <div class="game-details-container">
    <!-- 游戏标题 - 从数据库读取 -->
    <div class="game-title-section">
      <h1>${game.name}</h1>
    </div>

    <div class="game-details-main">
      <!-- 游戏图片与购买区域 -->
      <div class="game-media-purchase">
        <!-- 游戏图片展示区 -->
        <div class="game-media">
          <!-- 主图片 - 从数据库中读取 -->
          <img src="${pageContext.request.contextPath}/${game.imageUrl}" alt="${game.name}" class="game-main-image">
        </div>

        <!-- 游戏购买区域 -->
        <div class="game-purchase">
          <div class="game-purchase-box">
            <h2>购买 ${game.name}</h2>

            <div class="game-pricing">
              <c:choose>
                <c:when test="${game.price.doubleValue() == 0}">
                  <div class="game-price free">免费畅玩</div>
                </c:when>
                <c:when test="${game.hasDiscount()}">
                  <div class="game-discount">-${game.discountPercentage}%</div>
                  <div class="game-price discounted">
                    <span class="original">¥${game.price}</span>
                    <span class="current">¥${game.discountedPrice}</span>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="game-price">¥${game.price}</div>
                </c:otherwise>
              </c:choose>
            </div>

            <div class="game-actions">
              <c:choose>
                <c:when test="${ownsGame}">
                  <button class="layui-btn layui-btn-disabled">已拥有</button>
                </c:when>
                <c:when test="${inCart}">
                  <button class="layui-btn layui-btn-disabled">已在购物车</button>
                  <a href="${pageContext.request.contextPath}/cart/view" class="layui-btn layui-btn-normal">查看购物车</a>
                </c:when>
                <c:otherwise>
                  <!-- 修改这里：添加data-logged-in属性以便前端JS检查 -->
                  <button class="layui-btn" id="add-to-cart" data-game-id="${game.gameId}" data-logged-in="${not empty sessionScope.user}">加入购物车</button>
                </c:otherwise>
              </c:choose>
            </div>
          </div>

          <!-- 游戏标签 -->
          <div class="game-tags">
            <div class="tag">发行日期: <fmt:formatDate value="${game.releaseDate}" pattern="yyyy年MM月dd日" /></div>
            <div class="tag">开发商: ${game.developer}</div>
            <div class="tag">发行商: ${game.publisher}</div>
          </div>
        </div>
      </div>

      <!-- 游戏描述 -->
      <div class="game-description">
        <h2>关于这款游戏</h2>
        <div class="description-text">
          ${game.description}
        </div>
      </div>
    </div>
  </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
<script>
  layui.use(['layer', 'jquery'], function() {
    var layer = layui.layer;
    var $ = layui.jquery;

    // 加入购物车功能
    $('#add-to-cart').on('click', function() {
      var gameId = $(this).data('game-id');
      var isLoggedIn = $(this).data('logged-in');

      // 检查用户是否登录
      if (!isLoggedIn) {
        // 如果未登录，提示用户并重定向到登录页面
        layer.msg('请先登录再将游戏加入购物车', {
          icon: 0,
          time: 1500
        }, function() {
          // 将当前URL存储在会话存储中，以便登录后可以返回
          sessionStorage.setItem('redirectAfterLogin', window.location.href);
          // 重定向到登录页面
          window.location.href = '${pageContext.request.contextPath}/user/login';
        });
        return;
      }

      // 用户已登录，正常添加到购物车
      $.ajax({
        url: '${pageContext.request.contextPath}/cart/add',
        method: 'POST',
        data: {gameId: gameId},
        success: function(res) {
          if(res.success) {
            layer.msg(res.message);

            // 更新购物车数量
            $('.cart-count').text(res.cartItemCount);
            if($('.cart-count').length === 0) {
              $('.cart-btn').append('<span class="cart-count">' + res.cartItemCount + '</span>');
            }

            // 更新按钮状态
            $('#add-to-cart').replaceWith('<button class="layui-btn layui-btn-disabled">已在购物车</button>' +
                    '<a href="${pageContext.request.contextPath}/cart/view" class="layui-btn layui-btn-normal">查看购物车</a>');
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