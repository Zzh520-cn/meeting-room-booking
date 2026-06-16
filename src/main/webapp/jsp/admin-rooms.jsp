<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>会议室管理</title>
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

    <div class="page-toolbar">
        <span class="section-meta">共 ${fn:length(rooms)} 间会议室</span>
        <button class="btn btn-primary" onclick="openModal('addRoomModal')">+ 添加会议室</button>
    </div>

    <div class="card">
        <div class="card-header">会议室管理</div>

        <c:if test="${empty rooms}">
            <div class="empty-state">
                <div class="empty-icon">&square;</div>
                <h3>还没有会议室</h3>
                <p>点击上方按钮添加第一间会议室</p>
            </div>
        </c:if>

        <c:if test="${not empty rooms}">
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>名称</th>
                            <th>容量</th>
                            <th>设备</th>
                            <th>位置</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${rooms}" var="room">
                            <tr>
                                <td style="color:#9ca3af;font-size:12px;">#${room.id}</td>
                                <td><strong>${room.name}</strong></td>
                                <td>${room.capacity} 人</td>
                                <td>${room.equipment != null ? room.equipment : '-'}</td>
                                <td>${room.location != null ? room.location : '-'}</td>
                                <td>
                                    <c:if test="${room.isAvailable()}">
                                        <span class="badge badge-available">可用</span>
                                    </c:if>
                                    <c:if test="${!room.isAvailable()}">
                                        <span class="badge badge-maintenance">维护中</span>
                                    </c:if>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <button class="btn btn-outline btn-sm"
                                                onclick="openEditModal('${room.id}','${room.name}','${room.capacity}','${room.equipment}','${room.location}','${room.status}')">
                                            编辑
                                        </button>
                                        <form action="${pageContext.request.contextPath}/room" method="post"
                                              style="display:inline;"
                                              onsubmit="return confirmAction('确定要删除该会议室吗？关联的预约也会被删除。')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${room.id}">
                                            <button type="submit" class="btn btn-danger btn-sm">删除</button>
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
</div>

<!-- 添加会议室模态框 -->
<div class="modal-overlay" id="addRoomModal">
    <div class="modal-box">
        <h3>添加会议室</h3>
        <form action="${pageContext.request.contextPath}/room" method="post">
            <input type="hidden" name="action" value="add">

            <div class="form-row">
                <div class="form-group">
                    <label>名称 *</label>
                    <input type="text" name="name" required placeholder="例如：星辰厅">
                </div>
                <div class="form-group">
                    <label>容纳人数 *</label>
                    <input type="number" name="capacity" value="10" min="1" required>
                </div>
            </div>

            <div class="form-group">
                <label>设备</label>
                <input type="text" name="equipment" placeholder="例如：投影仪、白板、视频会议">
            </div>

            <div class="form-group">
                <label>位置</label>
                <input type="text" name="location" placeholder="例如：3楼A区301">
            </div>

            <div class="btn-row">
                <button type="button" class="btn btn-default" onclick="closeModal('addRoomModal')">取消</button>
                <button type="submit" class="btn btn-primary">确认添加</button>
            </div>
        </form>
    </div>
</div>

<!-- 编辑会议室模态框 -->
<div class="modal-overlay" id="editRoomModal">
    <div class="modal-box">
        <h3>编辑会议室</h3>
        <form action="${pageContext.request.contextPath}/room" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="id" id="editId">

            <div class="form-row">
                <div class="form-group">
                    <label>名称 *</label>
                    <input type="text" name="name" id="editName" required>
                </div>
                <div class="form-group">
                    <label>容纳人数 *</label>
                    <input type="number" name="capacity" id="editCapacity" min="1" required>
                </div>
            </div>

            <div class="form-group">
                <label>设备</label>
                <input type="text" name="equipment" id="editEquipment">
            </div>

            <div class="form-group">
                <label>位置</label>
                <input type="text" name="location" id="editLocation">
            </div>

            <div class="form-group">
                <label>状态</label>
                <select name="status" id="editStatus">
                    <option value="available">可用</option>
                    <option value="maintenance">维护中</option>
                </select>
            </div>

            <div class="btn-row">
                <button type="button" class="btn btn-default" onclick="closeModal('editRoomModal')">取消</button>
                <button type="submit" class="btn btn-primary">保存更改</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function openEditModal(id, name, capacity, equipment, location, status) {
    document.getElementById('editId').value = id;
    document.getElementById('editName').value = name;
    document.getElementById('editCapacity').value = capacity;
    document.getElementById('editEquipment').value = (equipment === 'null' || equipment === '') ? '' : equipment;
    document.getElementById('editLocation').value = (location === 'null' || location === '') ? '' : location;
    document.getElementById('editStatus').value = status;
    openModal('editRoomModal');
}
</script>
</body>
</html>
