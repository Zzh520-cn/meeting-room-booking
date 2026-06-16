<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>预约审批</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <span class="brand">会议室预约</span>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/admin?action=approval">审批管理</a>
        <a href="${pageContext.request.contextPath}/room?action=manage">会议室管理</a>
    </div>
    <div class="user-info">
        <span>${sessionScope.user.realName} &middot; 管理员</span>
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

    <!-- 待审批 -->
    <div class="card">
        <div class="card-header" style="border-bottom-color:#fcd34d;">
            待审批
            <c:if test="${not empty pendingList}">
                <span class="badge-count">${pendingList.size()}</span>
            </c:if>
        </div>

        <c:if test="${empty pendingList}">
            <div class="empty-state">
                <div class="empty-icon">&check;</div>
                <h3>暂无待审批的预约</h3>
                <p>所有预约都已处理完毕</p>
            </div>
        </c:if>

        <c:if test="${not empty pendingList}">
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>申请人</th>
                            <th>会议室</th>
                            <th>主题</th>
                            <th>时间</th>
                            <th>人数</th>
                            <th>用途</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${pendingList}" var="r">
                            <tr class="row-pending">
                                <td>${r.id}</td>
                                <td>${r.userName}</td>
                                <td><strong>${r.roomName}</strong></td>
                                <td>${r.title}</td>
                                <td>
                                    <fmt:formatDate value="${r.startTime}" pattern="MM-dd HH:mm"/>
                                    &ensp;&ndash;&ensp;
                                    <fmt:formatDate value="${r.endTime}" pattern="HH:mm"/>
                                </td>
                                <td>${r.attendees} 人</td>
                                <td style="max-width:140px;">${r.purpose}</td>
                                <td>
                                    <div class="btn-group">
                                        <form action="${pageContext.request.contextPath}/admin" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="approve">
                                            <input type="hidden" name="id" value="${r.id}">
                                            <button type="submit" class="btn btn-success btn-sm"
                                                    onclick="return confirmAction('确定批准该预约吗？')">
                                                批准
                                            </button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/admin" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="reject">
                                            <input type="hidden" name="id" value="${r.id}">
                                            <button type="submit" class="btn btn-danger btn-sm"
                                                    onclick="return confirmAction('确定拒绝该预约吗？')">
                                                拒绝
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>

    <!-- 全部记录 -->
    <div class="card">
        <div class="card-header">全部预约记录</div>

        <c:if test="${empty allReservations}">
            <div class="empty-state">
                <div class="empty-icon">&square;</div>
                <h3>暂无预约记录</h3>
                <p>系统还没有任何预约数据</p>
            </div>
        </c:if>

        <c:if test="${not empty allReservations}">
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>申请人</th>
                            <th>会议室</th>
                            <th>主题</th>
                            <th>开始</th>
                            <th>结束</th>
                            <th>人数</th>
                            <th>状态</th>
                            <th>提交时间</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${allReservations}" var="r">
                            <tr class="row-${r.status}">
                                <td>${r.id}</td>
                                <td>${r.userName}</td>
                                <td><strong>${r.roomName}</strong></td>
                                <td>${r.title}</td>
                                <td><fmt:formatDate value="${r.startTime}" pattern="MM-dd HH:mm"/></td>
                                <td><fmt:formatDate value="${r.endTime}" pattern="MM-dd HH:mm"/></td>
                                <td>${r.attendees} 人</td>
                                <td><span class="badge badge-${r.status}">${r.statusText}</span></td>
                                <td><fmt:formatDate value="${r.createTime}" pattern="MM-dd HH:mm"/></td>
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
