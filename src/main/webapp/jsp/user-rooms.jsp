<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>会议室列表</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <span class="brand">会议室预约</span>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/room?action=list">会议室列表</a>
        <a href="${pageContext.request.contextPath}/reservation?action=book">预约会议室</a>
        <a href="${pageContext.request.contextPath}/reservation">我的预约</a>
    </div>
    <div class="user-info">
        <span>${sessionScope.user.realName}</span>
        <a href="${pageContext.request.contextPath}/login?action=logout" class="btn-logout">退出</a>
    </div>
</nav>

<div class="container">

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">${sessionScope.message}</div>
        <c:remove var="message" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session" />
    </c:if>

    <div class="card">
        <div class="card-header">
            可用会议室
            <span style="font-weight:400;color:#9ca3af;font-size:14px;">${fn:length(rooms)} 间</span>
        </div>

        <c:if test="${empty rooms}">
            <div class="empty-state">
                <div class="empty-icon">&square;</div>
                <h3>暂无可用的会议室</h3>
                <p>所有会议室正在维护中，请稍后再来查看</p>
            </div>
        </c:if>

        <div class="room-grid">
            <c:forEach items="${rooms}" var="room">
                <div class="room-card">
                    <div class="room-accent"></div>
                    <div class="room-body">
                        <h3>${room.name}</h3>
                        <div class="room-info">
                            <p><span>位置</span> ${room.location != null ? room.location : '未指定'}</p>
                            <p><span>容量</span> ${room.capacity} 人</p>
                            <p><span>设备</span> ${room.equipment != null ? room.equipment : '无'}</p>
                            <p>
                                <span>状态</span>
                                <span class="badge badge-available">可用</span>
                            </p>
                        </div>
                        <div class="room-action">
                            <a href="${pageContext.request.contextPath}/reservation?action=book&roomId=${room.id}"
                               class="btn btn-primary btn-sm">预约</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
