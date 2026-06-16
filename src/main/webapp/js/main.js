/**
 * 会议室预约管理系统 — 前端交互脚本
 * 「清韵」设计语言
 */

// ============================================================
//  Toast 通知系统
// ============================================================

function showToast(message, type) {
  type = type || 'info';

  var container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  var icons = { success: '✓', error: '✗', warning: '⚠', info: 'ℹ' };

  var toast = document.createElement('div');
  toast.className = 'toast toast-' + type;
  toast.innerHTML =
    '<span>' + (icons[type] || '') + '</span>' +
    '<span>' + escapeHtml(message) + '</span>' +
    '<button class="toast-close" onclick="dismissToast(this.parentElement)">×</button>';

  container.appendChild(toast);

  // 最多同时显示 4 条，移除最早的
  var allToasts = container.querySelectorAll('.toast:not(.removing)');
  if (allToasts.length > 4) {
    dismissToast(allToasts[0]);
  }

  // 4.5 秒后自动消失
  setTimeout(function () {
    dismissToast(toast);
  }, 4500);
}

function dismissToast(toast) {
  if (!toast || toast.classList.contains('removing')) return;
  toast.classList.add('removing');
  setTimeout(function () {
    if (toast.parentNode) toast.parentNode.removeChild(toast);
  }, 260);
}

function escapeHtml(str) {
  var div = document.createElement('div');
  div.appendChild(document.createTextNode(str));
  return div.innerHTML;
}

// 页面加载时，把 JSP session 消息转为 toast
window.addEventListener('DOMContentLoaded', function () {
  var alerts = document.querySelectorAll('.alert');
  alerts.forEach(function (alert) {
    var type = 'info';
    if (alert.classList.contains('alert-success')) type = 'success';
    if (alert.classList.contains('alert-error'))   type = 'error';
    showToast(alert.textContent.trim(), type);
    alert.remove();
  });
});

// ============================================================
//  导航栏自动高亮
// ============================================================

window.addEventListener('DOMContentLoaded', function () {
  var currentPath = window.location.pathname + window.location.search;
  var links = document.querySelectorAll('.navbar .nav-links a');
  var bestMatch = null;
  var bestLen = 0;

  links.forEach(function (link) {
    var href = link.getAttribute('href');
    if (href && currentPath.indexOf(href) >= 0 && href.length > bestLen) {
      bestMatch = link;
      bestLen = href.length;
    }
  });
  if (bestMatch) {
    links.forEach(function (l) { l.classList.remove('active'); });
    bestMatch.classList.add('active');
  }
});

// ============================================================
//  会议室装饰色映射 — 东方色卡
// ============================================================

var ROOM_COLORS = {
  '星': '#475569',   // 星 → 岩灰
  '云': '#94a3b8',   // 云 → 云灰
  '海': '#0d7b6e',   // 海 → 青碧
  '朝': '#d97756',   // 朝 → 暖橙
  '阳': '#d9a456',   // 阳 → 琥珀
  '竹': '#65a30d',   // 竹 → 翠绿
  '韵': '#8b5cf6',   // 韵 → 紫烟
  '聚': '#b45389',   // 聚 → 嫣红
  '贤': '#6366f1',   // 贤 → 靛青
  '听': '#6d8c7e',   // 听 → 苔青
  '风': '#78716c'    // 风 → 暖灰
};

function accentForRoom(name) {
  if (!name) return '#0d7b6e';
  for (var i = 0; i < name.length; i++) {
    var ch = name.charAt(i);
    if (ROOM_COLORS[ch]) return ROOM_COLORS[ch];
  }
  var hash = 0;
  for (var j = 0; j < name.length; j++) hash = name.charCodeAt(j) + ((hash << 5) - hash);
  var hue = (hash % 60) + 170; // 青绿色系
  return 'hsl(' + hue + ', 40%, 38%)';
}

window.addEventListener('DOMContentLoaded', function () {
  var cards = document.querySelectorAll('.room-card');
  cards.forEach(function (card) {
    var h3 = card.querySelector('h3');
    var name = h3 ? h3.textContent.trim() : '';
    var accent = card.querySelector('.room-accent');
    if (accent) {
      accent.style.background = accentForRoom(name);
    }
  });
});

// ============================================================
//  模态框
// ============================================================

function openModal(id) {
  var overlay = document.getElementById(id);
  if (!overlay) return;
  overlay.classList.add('active');
  document.body.style.overflow = 'hidden';
  var firstInput = overlay.querySelector(
    'input[type="text"], input[type="number"], input[type="datetime-local"]'
  );
  if (firstInput) setTimeout(function () { firstInput.focus(); }, 150);
}

function closeModal(id) {
  var overlay = document.getElementById(id);
  if (!overlay) return;
  overlay.classList.remove('active');
  // 如果所有 modal 都关了，恢复页面滚动
  if (!document.querySelector('.modal-overlay.active')) {
    document.body.style.overflow = '';
  }
}

// 点击遮罩层关闭
document.addEventListener('click', function (e) {
  if (e.target.classList.contains('modal-overlay')) {
    closeModal(e.target.id);
  }
});

// ESC 关闭
document.addEventListener('keydown', function (e) {
  if (e.key === 'Escape') {
    var active = document.querySelector('.modal-overlay.active');
    if (active) closeModal(active.id);
  }
});

// ============================================================
//  确认对话框
// ============================================================

function confirmAction(message) {
  return window.confirm(message);
}

// ============================================================
//  表单验证
// ============================================================

function validateBookingForm() {
  var title = document.getElementById('title');
  var startTime = document.getElementById('startTime');
  var endTime = document.getElementById('endTime');
  var attendees = document.getElementById('attendees');
  var valid = true;

  clearFieldErrors();

  if (!title || !title.value.trim()) {
    showFieldError(title, '请输入会议主题');
    valid = false;
  }
  if (!startTime || !startTime.value) {
    showFieldError(startTime, '请选择开始时间');
    valid = false;
  }
  if (!endTime || !endTime.value) {
    showFieldError(endTime, '请选择结束时间');
    valid = false;
  }
  if (startTime && endTime && startTime.value && endTime.value &&
      startTime.value >= endTime.value) {
    showFieldError(endTime, '结束时间必须晚于开始时间');
    valid = false;
  }
  if (!attendees || !attendees.value || parseInt(attendees.value) <= 0) {
    showFieldError(attendees, '请输入有效的参会人数');
    valid = false;
  }
  return valid;
}

function showFieldError(input, message) {
  if (!input) return;
  input.classList.add('input-error');
  var formGroup = input.closest('.form-group');
  if (formGroup) {
    var hint = formGroup.querySelector('.field-hint');
    if (!hint) {
      hint = document.createElement('div');
      hint.className = 'field-hint';
      formGroup.appendChild(hint);
    }
    hint.textContent = message;
    hint.classList.add('visible');
  }
}

function clearFieldErrors() {
  document.querySelectorAll('.input-error').forEach(function (el) {
    el.classList.remove('input-error');
  });
  document.querySelectorAll('.field-hint.visible').forEach(function (el) {
    el.classList.remove('visible');
  });
}

// 输入时自动清除该字段错误
document.addEventListener('input', function (e) {
  if (e.target.classList.contains('input-error')) {
    e.target.classList.remove('input-error');
    var fg = e.target.closest('.form-group');
    if (fg) {
      var hint = fg.querySelector('.field-hint');
      if (hint) hint.classList.remove('visible');
    }
  }
});

// ============================================================
//  datetime-local 联动
// ============================================================

window.addEventListener('DOMContentLoaded', function () {
  var startInput = document.getElementById('startTime');
  if (startInput) {
    var now = new Date();
    var tzOffset = now.getTimezoneOffset() * 60000;
    var localTime = new Date(now.getTime() - tzOffset);
    startInput.min = localTime.toISOString().slice(0, 16);

    startInput.addEventListener('change', function () {
      var endInput = document.getElementById('endTime');
      if (endInput && startInput.value) {
        endInput.min = startInput.value;
        if (endInput.value && endInput.value <= startInput.value) {
          endInput.value = '';
        }
      }
    });
  }
});

// ============================================================
//  表格行点击视觉反馈（可选）
// ============================================================

document.addEventListener('click', function (e) {
  var row = e.target.closest('table tbody tr');
  if (!row) return;
  // 如果点击的是操作按钮所在的行，不触发行选择效果
  if (e.target.closest('button, a, form')) return;
  // 轻量级点击反馈
  row.style.transition = 'background 0.15s';
  row.style.background = '#f0f4f3';
  setTimeout(function () { row.style.background = ''; }, 200);
});
