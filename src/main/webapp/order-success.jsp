<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>订单成功 - Steam Clone</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
  <style>
    .success-container {
      max-width: 800px;
      margin: 50px auto;
    }
    .success-icon {
      text-align: center;
      margin: 30px 0;
    }
    .success-message {
      text-align: center;
      margin-bottom: 30px;
    }
    .success-message h2 {
      color: #333;
      margin-bottom: 15px;
    }
    .success-actions {
      text-align: center;
      margin-top: 20px;
    }
    .success-actions .layui-btn {
      margin: 0 10px;
    }
  </style>
</head>
<body>
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
  <div class="success-container">
    <div class="layui-card">
      <div class="layui-card-header">
        <h1>订单成功完成</h1>
      </div>
      <div class="layui-card-body">
        <div class="success-icon">
          <i class="layui-icon layui-icon-ok-circle" style="font-size: 72px; color: #5cb85c;"></i>
        </div>

        <div class="success-message">
          <h2>感谢您的购买！</h2>
          <p>您的游戏已成功添加到您的游戏库。</p>
        </div>

        <div class="success-actions">
          <!-- 修改为 -->
          <a href="${pageContext.request.contextPath}/user/library" class="layui-btn layui-btn-lg">
            前往我的游戏库
          </a>
          <a href="${pageContext.request.contextPath}/" class="layui-btn layui-btn-lg layui-btn-primary">
            继续购物
          </a>
        </div>
      </div>
    </div>
  </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
</body>
</html>