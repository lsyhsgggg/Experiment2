<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Steam Clone - 游戏商城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <!-- 特色轮播图 -->
    <div class="featured-carousel layui-carousel" id="featured-carousel">
        <div carousel-item>
            <c:forEach var="featuredGame" items="${featuredGames}" varStatus="status">
                <div class="carousel-item">
                    <a href="${pageContext.request.contextPath}/game/${featuredGame.gameId}">
                        <!-- 修改这里：使用carousel文件夹中的游戏图片 -->
                        <img src="${pageContext.request.contextPath}/images/carousel/game_${featuredGame.gameId}.jpg" alt="${featuredGame.name}">
                        <div class="carousel-caption">
                            <h2>${featuredGame.name}</h2>
                            <p>${featuredGame.description}</p>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- 游戏列表 -->
    <section class="games-section">
        <div class="section-header">
            <h2 class="section-title">
                <c:choose>
                    <c:when test="${not empty keyword}">搜索结果：${keyword}</c:when>
                    <c:otherwise>推荐游戏</c:otherwise>
                </c:choose>
            </h2>
        </div>

        <div class="games-grid">
            <c:choose>
                <c:when test="${empty games}">
                    <div class="no-results">
                        <p>没有找到游戏。</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="game" items="${games}">
                        <div class="game-card">
                            <a href="${pageContext.request.contextPath}/game/${game.gameId}">
                                <div class="game-image">
                                    <!-- 从数据库读取游戏图片URL -->
                                    <img src="${pageContext.request.contextPath}/${game.imageUrl}" alt="${game.name}">
                                    <c:if test="${game.hasDiscount()}">
                                        <div class="discount-badge">-${game.discountPercentage}%</div>
                                    </c:if>
                                </div>
                                <div class="game-info">
                                    <h3 class="game-title">${game.name}</h3>
                                    <div class="game-price">
                                        <c:if test="${game.hasDiscount()}">
                                            <span class="original-price">¥${game.price}</span>
                                        </c:if>
                                        <span class="current-price">
                                                <c:choose>
                                                    <c:when test="${game.price.doubleValue() == 0}">免费畅玩</c:when>
                                                    <c:otherwise>¥${game.discountedPrice}</c:otherwise>
                                                </c:choose>
                                            </span>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 分页 -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <ul>
                    <c:if test="${currentPage > 1}">
                        <li>
                            <a href="?page=${currentPage - 1}${not empty keyword ? '&search=' : ''}${keyword}">上一页</a>
                        </li>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="${i == currentPage ? 'active' : ''}">
                            <a href="?page=${i}${not empty keyword ? '&search=' : ''}${keyword}">${i}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li>
                            <a href="?page=${currentPage + 1}${not empty keyword ? '&search=' : ''}${keyword}">下一页</a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </c:if>
    </section>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
<script>
    layui.use(['carousel', 'element'], function() {
        var carousel = layui.carousel;

        // 轮播图配置
        carousel.render({
            elem: '#featured-carousel',
            width: '100%',
            height: '400px',
            interval: 5000,
            arrow: 'always'
        });
    });
</script>
</body>
</html>