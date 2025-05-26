<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - 页面未找到</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="error-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="error-container">
        <div class="error-code">404</div>
        <h1>页面未找到</h1>
        <p>您访问的页面不存在或已被移除。</p>
        <a href="${pageContext.request.contextPath}/" class="layui-btn">返回首页</a>
    </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
</body>
</html>