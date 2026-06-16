<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>预约会议室</title>
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

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session" />
    </c:if>

    <div class="card">
        <div class="card-header">提交预约申请</div>

        <form action="${pageContext.request.contextPath}/reservation" method="post"
              onsubmit="return validateBookingForm()">
            <input type="hidden" name="action" value="book">

            <div class="form-row">
                <div class="form-group">
                    <label for="roomId">选择会议室 *</label>
                    <select id="roomId" name="roomId" required>
                        <option value="">-- 请选择会议室 --</option>
                        <c:forEach items="${rooms}" var="r">
                            <option value="${r.id}"
                                    <c:if test="${room != null && room.id == r.id}">selected</c:if>
                                    <c:if test="${formData != null && formData.roomId == r.id}">selected</c:if>>
                                    ${r.name}  ${r.location}  ${r.capacity}人
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="attendees">参会人数 *</label>
                    <input type="number" id="attendees" name="attendees"
                           value="${formData != null ? formData.attendees : ''}"
                           placeholder="请输入人数" min="1" required>
                    <c:if test="${room != null}">
                        <small>该会议室最大容纳 ${room.capacity} 人</small>
                    </c:if>
                    <div class="field-hint"></div>
                </div>
            </div>

            <div class="form-group">
                <label for="title">会议主题 *</label>
                <input type="text" id="title" name="title"
                       value="${formData != null ? formData.title : ''}"
                       placeholder="例如：项目周会、需求评审、技术分享">
                <div class="field-hint"></div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="startTime">开始时间 *</label>
                    <input type="datetime-local" id="startTime" name="startTime"
                           value="${formData != null ? formData.startTime : ''}" required>
                    <div class="field-hint"></div>
                </div>
                <div class="form-group">
                    <label for="endTime">结束时间 *</label>
                    <input type="datetime-local" id="endTime" name="endTime"
                           value="${formData != null ? formData.endTime : ''}" required>
                    <div class="field-hint"></div>
                </div>
            </div>

            <div class="form-group">
                <label for="purpose">会议用途 / 备注</label>
                <textarea id="purpose" name="purpose" rows="3"
                          placeholder="简要描述会议内容和目的（选填）">${formData != null ? formData.purpose : ''}</textarea>
            </div>

            <div class="form-group" style="text-align:center;margin-bottom:0;">
                <button type="submit" class="btn btn-primary" style="padding:10px 36px;font-size:14.5px;">
                    提交预约，等待审批
                </button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
