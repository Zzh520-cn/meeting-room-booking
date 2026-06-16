<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 — 会议室预约系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">

<div class="auth-box">
    <h2>用户注册</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="hidden" name="action" value="register">

        <div class="form-row">
            <div class="form-group">
                <label for="username">用户名 *</label>
                <input type="text" id="username" name="username"
                       value="${regUser.username}" placeholder="4-20位字母或数字" required>
            </div>
            <div class="form-group">
                <label for="realName">真实姓名 *</label>
                <input type="text" id="realName" name="realName"
                       value="${regUser.realName}" placeholder="请输入真实姓名" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="password">密码 *</label>
                <input type="password" id="password" name="password"
                       placeholder="请输入密码" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">确认密码 *</label>
                <input type="password" id="confirmPassword" name="confirmPassword"
                       placeholder="再次输入密码" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="phone">联系电话</label>
                <input type="text" id="phone" name="phone"
                       value="${regUser.phone}" placeholder="选填">
            </div>
            <div class="form-group">
                <label for="email">邮箱</label>
                <input type="email" id="email" name="email"
                       value="${regUser.email}" placeholder="选填">
            </div>
        </div>

        <button type="submit" class="btn-submit" onclick="return validateRegister()">注 册</button>
    </form>

    <div class="switch-link">
        已有账号？<a href="${pageContext.request.contextPath}/login">返回登录</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function validateRegister() {
    var pwd = document.getElementById('password');
    var confirm = document.getElementById('confirmPassword');
    if (pwd && confirm && pwd.value !== confirm.value) {
        showToast('两次输入的密码不一致', 'error');
        return false;
    }
    return true;
}
</script>
</body>
</html>
