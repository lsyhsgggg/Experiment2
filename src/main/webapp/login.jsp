<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="login-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="auth-container">
        <div class="auth-box">
            <h2>登录到您的账户</h2>

            <!-- 错误消息显示 -->
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                        ${errorMessage}
                </div>
            </c:if>

            <form class="layui-form" action="${pageContext.request.contextPath}/user/login" method="post">
                <!-- 隐藏字段用于存储目标URL -->
                <input type="hidden" name="targetUrl" value="${param.targetUrl}">

                <div class="layui-form-item">
                    <label class="layui-form-label">用户名</label>
                    <div class="layui-input-block">
                        <input type="text" name="username" required lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="password" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" lay-submit>登录</button>
                    </div>
                </div>
            </form>

            <div class="auth-links">
                <p>还没有账户？<a href="${pageContext.request.contextPath}/user/register">立即注册</a></p>
            </div>
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

        // 表单验证
        form.verify({
            required: function(value) {
                if(value.length === 0) {
                    return '必填项不能为空';
                }
            }
        });
    });
    // 获取登录后重定向URL - 登录页面的script中
    $(document).ready(function() {
        // 从会话存储中获取重定向URL
        var redirectUrl = sessionStorage.getItem('redirectAfterLogin');
        if (redirectUrl) {
            // 将地址添加到隐藏字段
            $('<input>').attr({
                type: 'hidden',
                name: 'redirectUrl',
                value: redirectUrl
            }).appendTo('form');

            // 清除会话存储中的数据
            sessionStorage.removeItem('redirectAfterLogin');
        }
    });
</script>
</body>
</html>