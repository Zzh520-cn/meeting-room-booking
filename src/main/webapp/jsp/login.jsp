<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 — 会议室预约系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">

<div class="auth-box">
    <h2>会议室预约</h2>

    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username"
                   placeholder="请输入用户名" autocomplete="username" required>
        </div>
        <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" name="password"
                   placeholder="请输入密码" autocomplete="current-password" required>
        </div>
        <button type="submit" class="btn-submit">登 录</button>
    </form>

    <div class="switch-link">
        还没有账号？<a href="${pageContext.request.contextPath}/jsp/register.jsp">立即注册</a>
    </div>

    <div class="test-accounts">
        <p><strong>测试账号</strong></p>
        <p>管理员：admin / 123456</p>
        <p>普通用户：zhangsan / 123456</p>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
