<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的游戏库 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <style>
        .library-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #3a506a;
        }

        .library-title {
            font-size: 24px;
            margin: 0;
            color: #c6d4df;
        }

        .library-count {
            color: #7193a6;
            font-size: 14px;
        }

        .library-empty {
            text-align: center;
            padding: 40px;
            background-color: #16202d;
            border-radius: 4px;
        }

        .library-empty p {
            font-size: 18px;
            color: #7193a6;
        }

        .library-games {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
        }

        .library-game {
            background-color: #16202d;
            border-radius: 4px;
            overflow: hidden;
            transition: transform 0.2s;
        }

        .library-game:hover {
            transform: translateY(-5px);
        }

        .library-game-image {
            width: 100%;
            aspect-ratio: 16/9;
            object-fit: cover;
        }

        .library-game-info {
            padding: 15px;
        }

        .library-game-title {
            font-size: 16px;
            margin: 0 0 5px;
            color: #c6d4df;
        }

        .library-game-meta {
            color: #7193a6;
            font-size: 12px;
        }

        .library-game-actions {
            margin-top: 10px;
            display: flex;
            gap: 10px;
        }
    </style>
</head>
<body>
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="library-header">
        <h1 class="library-title">我的游戏库</h1>

        <!-- 只有当有游戏时才显示游戏数量 -->
        <c:if test="${not empty userGames and fn:length(userGames) > 0}">
            <div class="library-count">共 ${fn:length(userGames)} 款游戏</div>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${empty userGames}">
            <div class="library-empty">
                <p>您的游戏库中还没有游戏</p>
                <a href="${pageContext.request.contextPath}/" class="layui-btn layui-btn-normal">浏览商店</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="library-games">
                <c:forEach var="game" items="${userGames}">
                    <div class="library-game">
                        <img src="${pageContext.request.contextPath}/${game.imageUrl}" alt="${game.name}" class="library-game-image">
                        <div class="library-game-info">
                            <h3 class="library-game-title">${game.name}</h3>
                            <div class="library-game-meta">
                                <div>购买日期: ${game.purchaseDate}</div>
                            </div>
                            <div class="library-game-actions">
                                <a href="#" class="layui-btn layui-btn-sm">下载</a>
                                <a href="${pageContext.request.contextPath}/game/${game.gameId}" class="layui-btn layui-btn-sm layui-btn-primary">查看商店页面</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
</body>
</html>