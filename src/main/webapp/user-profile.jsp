<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料 - Steam Clone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/layui/2.5.7/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="profile-page">
<!-- 包含页头 -->
<jsp:include page="/components/header.jsp" />

<main class="main-content">
    <div class="profile-container">
        <div class="profile-sidebar">
            <div class="profile-avatar">
                <!-- 替换为用户头像 -->
                <img src="${pageContext.request.contextPath}/${user.avatarUrl}" alt="${user.username}" class="avatar-image">
            </div>
            <div class="profile-username">${user.username}</div>
            <div class="profile-links">
                <a href="${pageContext.request.contextPath}/user/profile" class="active">个人资料</a>
                <a href="${pageContext.request.contextPath}/user/library">我的游戏库</a>
                <a href="${pageContext.request.contextPath}/order/history">订单历史</a>
                <a href="${pageContext.request.contextPath}/user/logout">退出登录</a>
            </div>
        </div>

        <div class="profile-content">
            <h1>个人资料</h1>

            <!-- 消息显示 -->
            <c:if test="${not empty successMessage}">
                <div class="success-message">
                        ${successMessage}
                </div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                        ${errorMessage}
                </div>
            </c:if>

            <div class="profile-details">
                <div class="detail-item">
                    <div class="detail-label">用户名</div>
                    <div class="detail-value">${user.username}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">电子邮箱</div>
                    <div class="detail-value">${user.email}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">注册日期</div>
                    <div class="detail-value">
                        <fmt:formatDate value="${user.registerDate}" pattern="yyyy年MM月dd日" />
                    </div>
                </div>
            </div>

            <div class="profile-edit">
                <h2>编辑个人资料</h2>

                <form class="layui-form" action="${pageContext.request.contextPath}/user/update" method="post">
                    <div class="layui-form-item">
                        <label class="layui-form-label">电子邮箱</label>
                        <div class="layui-input-block">
                            <input type="email" name="email" required lay-verify="required|email" placeholder="请输入新的电子邮箱" autocomplete="off" class="layui-input" value="${user.email}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">当前密码</label>
                        <div class="layui-input-block">
                            <input type="password" name="currentPassword" placeholder="要更改密码，请先输入当前密码" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">新密码</label>
                        <div class="layui-input-block">
                            <input type="password" name="newPassword" lay-verify="password" placeholder="请输入新密码（若要更改）" autocomplete="off" class="layui-input" id="newPassword">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">确认新密码</label>
                        <div class="layui-input-block">
                            <input type="password" name="confirmPassword" lay-verify="confirmPassword" placeholder="请再次输入新密码" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit>保存更改</button>
                        </div>
                    </div>
                </form>
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
            password: function(value) {
                if(value && value.length < 6) {
                    return '密码必须至少6个字符';
                }
            },
            confirmPassword: function(value) {
                var password = $('#newPassword').val();
                if(password && value !== password) {
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
    });
</script>
</body>
</html>