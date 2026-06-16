<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的预约</title>
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
        <div class="card-header">我的预约记录</div>

        <c:if test="${empty reservations}">
            <div class="empty-state">
                <div class="empty-icon">&square;</div>
                <h3>还没有预约记录</h3>
                <p>选择一个会议室，开始你的第一次预约吧</p>
                <a href="${pageContext.request.contextPath}/reservation?action=book" class="btn btn-primary">去预约</a>
            </div>
        </c:if>

        <c:if test="${not empty reservations}">
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>会议室</th>
                            <th>会议主题</th>
                            <th>时间</th>
                            <th>人数</th>
                            <th>状态</th>
                            <th>提交时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${reservations}" var="r">
                            <tr class="row-${r.status}">
                                <td><strong>${r.roomName}</strong></td>
                                <td>${r.title}</td>
                                <td>
                                    <fmt:formatDate value="${r.startTime}" pattern="MM-dd HH:mm"/>
                                    &ensp;&ndash;&ensp;
                                    <fmt:formatDate value="${r.endTime}" pattern="HH:mm"/>
                                </td>
                                <td>${r.attendees} 人</td>
                                <td>
                                    <span class="badge badge-${r.status}">${r.statusText}</span>
                                </td>
                                <td><fmt:formatDate value="${r.createTime}" pattern="MM-dd HH:mm"/></td>
                                <td>
                                    <c:if test="${r.canCancel()}">
                                        <form action="${pageContext.request.contextPath}/reservation" method="post"
                                              style="display:inline;"
                                              onsubmit="return confirmAction('确定要取消这个预约吗？')">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="id" value="${r.id}">
                                            <button type="submit" class="btn btn-danger btn-sm">取消</button>
                                        </form>
                                    </c:if>
                                    <c:if test="${!r.canCancel()}">
                                        <span style="color:#d1d5db;font-size:12px;">&mdash;</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
