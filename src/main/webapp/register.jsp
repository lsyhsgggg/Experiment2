<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="register-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="auth-container">
        <div class="auth-box">
            <h2>创建新账户</h2>

            <!-- 错误消息显示 -->
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                        ${errorMessage}
                </div>
            </c:if>

            <form class="layui-form" action="${pageContext.request.contextPath}/user/register" method="post">
                <div class="layui-form-item">
                    <label class="layui-form-label">用户名</label>
                    <div class="layui-input-block">
                        <input type="text" name="username" required lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input" id="username">
                        <div class="username-status"></div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">电子邮箱</label>
                    <div class="layui-input-block">
                        <input type="email" name="email" required lay-verify="required|email" placeholder="请输入电子邮箱" autocomplete="off" class="layui-input" id="email">
                        <div class="email-status"></div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="password" required lay-verify="required|password" placeholder="请输入密码" autocomplete="off" class="layui-input" id="password">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">确认密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="confirmPassword" required lay-verify="required|confirmPassword" placeholder="请再次输入密码" autocomplete="off" class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" lay-submit>注册</button>
                    </div>
                </div>
            </form>

            <div class="auth-links">
                <p>已有账户？<a href="${pageContext.request.contextPath}/user/login">立即登录</a></p>
            </div>
        </div>
    </div>
</main>

<!-- 包含页脚 -->
<jsp:include page="/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/webjars/layui/2.5.7/layui.js"></script>
<script>
    layui.use(['form', 'layer', 'jquery'], function() {
        var form = layui.form;
        var layer = layui.layer;
        var $ = layui.jquery;

        // 表单验证
        form.verify({
            required: function(value) {
                if(value.length === 0) {
                    return '必填项不能为空';
                }
            },
            password: function(value) {
                if(value.length < 6) {
                    return '密码必须至少6个字符';
                }
            },
            confirmPassword: function(value) {
                var password = $('#password').val();
                if(value !== password) {
                    return '两次输入的密码不一致';
                }
            },
            email: function(value) {
                var emailPattern = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                if(!emailPattern.test(value)) {
                    return '请输入有效的电子邮箱地址';
                }
            }
        });

        // 实时检查用户名是否可用
        $('#username').on('blur', function() {
            var username = $(this).val();
            if(username) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/user/check-username',
                    method: 'GET',
                    data: {username: username},
                    success: function(res) {
                        if(res.available) {
                            $('.username-status').html('<span class="available">用户名可用</span>');
                        } else {
                            $('.username-status').html('<span class="not-available">用户名已被使用</span>');
                        }
                    }
                });
            }
        });

        // 实时检查邮箱是否可用
        $('#email').on('blur', function() {
            var email = $(this).val();
            if(email) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/user/check-email',
                    method: 'GET',
                    data: {email: email},
                    success: function(res) {
                        if(res.available) {
                            $('.email-status').html('<span class="available">邮箱可用</span>');
                        } else {
                            $('.email-status').html('<span class="not-available">邮箱已被使用</span>');
                        }
                    }
                });
            }
        });
    });
</script>
</body>
</html>