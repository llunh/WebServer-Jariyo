<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="mvc.model.RestaurantDAO, mvc.model.MenuDAO" %>
<%@ page import="mvc.model.RestaurantDTO, mvc.model.MenuDTO, java.util.ArrayList" %>
<%@ page import="mvc.model.ReviewDAO"%>
<%@ page import="mvc.model.ReviewDTO"%>
<%@ page import="mvc.model.ReviewImageDTO"%>
<%@ page import="mvc.model.UserDTO"%>
<%@ page import="mvc.model.FavoriteDAO"%>
<!DOCTYPE html>
<html>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    RestaurantDTO restaurant = RestaurantDAO.getInstance().getRestaurantById(id);
    ArrayList<MenuDTO> menus = MenuDAO.getInstance().getMenusByRestaurantId(id);
    ArrayList<ReviewDTO> reviews = mvc.model.ReviewDAO.getInstance().getReviewsByRestaurantId(id);
    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
    String sessionId  = (String)  session.getAttribute("sessionId");

    boolean isFav = false;
    if (loginUser != null) {
        isFav = FavoriteDAO.getInstance().isFavorite(loginUser.getId(), id);
    }
    String favClass = isFav ? "btn-warning" : "btn-outline-warning";
    String favText  = isFav ? "★ 관심 식당 취소" : "☆ 관심 식당 추가";
%>
<head>
<meta charset="UTF-8">
<link href="./resources/css/bootstrap.min.css" rel="stylesheet">
<title>식당 상세페이지</title>
<style>
    .restaurant-img { height: 300px; object-fit: cover; }
    .menu-img { height: 150px; object-fit: cover; }
</style>
</head>
<body>

<%-- 메뉴바 --%>
<jsp:include page="views/menu.jsp" />

<%-- 식당 정보 --%>
<div class="container mt-4">
    <img src="resources/images/<%=restaurant.getImageFilename()%>" class="restaurant-img w-100 rounded-3">
    <h2><%=restaurant.getName()%></h2>
    <p>카테고리: <%=restaurant.getCategory()%></p>
    <p>주소: <%=restaurant.getAddress()%></p>
    <p>전화: <%=restaurant.getPhone()%></p>
    <p>영업시간: <%=restaurant.getOpeningHours()%></p>

    <%-- 관심 식당 버튼 --%>
    <% if (loginUser != null) { %>
        <a href="<%=request.getContextPath()%>/FavoriteAction.do?restaurantId=<%=id%>"
           class="btn <%=favClass%> mt-2"><%=favText%></a>
    <% } %>
</div>

<%-- 메뉴 목록 --%>
<div class="container mt-4">
    <h3>메뉴</h3>
    <div class="row">
        <% for(MenuDTO menu : menus) { %>
        <div class="col-md-3 mb-3">
            <div class="card">
                <img src="resources/images/<%=menu.getImageFilename()%>" class="menu-img card-img-top">
                <div class="card-body">
                    <h6 class="card-title"><%=menu.getName()%></h6>
                    <p class="card-text"><%=menu.getPrice()%>원</p>
                    <p class="card-text"><%=menu.getDescription() != null ? menu.getDescription() : ""%></p>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>

<%-- 방문 리뷰 목록 --%>
<div class="container mt-4 mb-5">
    <h3>방문 리뷰</h3>
    <%
        if (reviews == null || reviews.isEmpty()) {
    %>
        <div class="alert alert-info">아직 작성된 리뷰가 없습니다.</div>
    <%
        } else {
            for (ReviewDTO review : reviews) {
    %>
        <div class="card mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <h6 class="card-title">
                        <% for (int s = 0; s < review.getRating(); s++) { out.print("⭐"); } %>
                        <small class="text-muted">(<%=review.getRating()%>/5)</small>
                    </h6>
                    <small class="text-muted"><%=review.getCreatedAt()%></small>
                </div>
                <p class="text-muted mb-1">작성자: <%=review.getUsername()%></p>
                <p class="card-text"><%=review.getContent()%></p>
                <%
                    ArrayList<ReviewImageDTO> imgs = review.getImages();
                    if (imgs != null && !imgs.isEmpty()) {
                        for (ReviewImageDTO img : imgs) {
                %>
                    <img src="<%=request.getContextPath()%>/uploads/<%=img.getFileName()%>"
                         alt="<%=img.getOriName()%>"
                         style="max-width:150px; max-height:150px; margin:4px; object-fit:cover;"
                         class="rounded">
                <%
                        }
                    }
                %>
            </div>
        </div>
    <%
            }
        }
    %>
    <div class="text-end mt-2">
        <a href="<%=request.getContextPath()%>/ReviewWriteForm.do"
           class="btn btn-primary">리뷰 작성하기</a>
    </div>
</div>

<%-- 예약 신청 --%>
<div class="container mt-4">
    <h3>예약 신청</h3>
    <%
        if (sessionId == null || sessionId.isEmpty()) {
    %>
        <div class="alert alert-warning">
            예약하려면 <a href="MemberLoginForm.do">로그인</a>이 필요합니다.
        </div>
    <%
        } else {
    %>
    <form action="reservationForm.jsp" method="get" onsubmit="return validateForm()">
        <input type="hidden" name="restaurantId" value="<%=restaurant.getId()%>">
        <div class="mb-3">
            <label>날짜</label>
            <input type="date" id="dateInput" name="date" class="form-control"
                   min="<%=java.time.LocalDate.now()%>"
                   onchange="filterTimeOptions(); checkAvailability()">
        </div>
        <div class="mb-3">
            <label>시간</label>
            <%
                String[] timeRange  = restaurant.getOpeningHours().split(" - ");
                int startHour       = Integer.parseInt(timeRange[0].split(":")[0]);
                int startMinute     = Integer.parseInt(timeRange[0].split(":")[1]);
                int endHour         = Integer.parseInt(timeRange[1].split(":")[0]);
                if (startMinute > 0) startHour++;
                
             // 오늘 날짜인 경우 현재 시간 이후만 표시
                java.util.Calendar now = java.util.Calendar.getInstance();
                int currentHour = now.get(java.util.Calendar.HOUR_OF_DAY);
                String today = java.time.LocalDate.now().toString();
            %>
            <select id="timeInput" name="time" class="form-control" onchange="checkAvailability()">
    <% for (int h = startHour; h < endHour; h++) { %>
        <%-- 오늘 날짜면 현재 시간 이후만 표시, 미래 날짜면 전체 표시 --%>
        <option value="<%=String.format("%02d:00", h)%>"
            <%-- JavaScript로 날짜 선택에 따라 동적으로 숨김 처리 --%>
            data-hour="<%=h%>">
            <%=String.format("%02d:00", h)%>
        </option>
    <% } %>
</select>
<small id="timeNote" class="text-muted"></small>
        </div>
        <div class="mb-3">
            <label>인원</label>
            <input type="number" name="partySize" min="1"
                   max="<%=restaurant.getMaxPartySize()%>" class="form-control">
        </div>

        <%-- AJAX 결과 표시 --%>
        <div id="availabilityResult" class="mb-3"></div>

        <button type="submit" id="submitBtn" class="btn btn-primary">예약 신청</button>
    </form>
    <%
        }
    %>
</div>

<script>
const restaurantId = <%=restaurant.getId()%>;

//날짜 선택 시 시간 옵션 필터링
function filterTimeOptions() {
    const dateInput = document.getElementById('dateInput').value;
    const today = new Date().toISOString().split('T')[0];
    const currentHour = new Date().getHours();
    const options = document.querySelectorAll('#timeInput option');
    let firstAvailable = null;

    options.forEach(opt => {
        const hour = parseInt(opt.getAttribute('data-hour'));
        if (dateInput === today && hour <= currentHour) {
            opt.disabled = true;
            opt.style.display = 'none';
        } else {
            opt.disabled = false;
            opt.style.display = '';
            if (!firstAvailable) firstAvailable = opt;
        }
    });

    // 첫 번째 가능한 시간으로 자동 선택
    if (firstAvailable) {
        firstAvailable.selected = true;
    }

    // 안내 메시지
    const note = document.getElementById('timeNote');
    if (dateInput === today) {
        note.textContent = '오늘은 현재 시간(' + currentHour + '시) 이후만 예약 가능합니다.';
    } else {
        note.textContent = '';
    }
    checkAvailability();
}



function checkAvailability() {
    const date = document.getElementById('dateInput').value;
    const time = document.getElementById('timeInput').value;
    if (!date || !time) return;

    fetch('<%=request.getContextPath()%>/ReservationCheck.do?restaurantId=' + restaurantId + '&date=' + date + '&time=' + time)
        .then(res => res.json())
        .then(data => {
            const resultDiv = document.getElementById('availabilityResult');
            const submitBtn = document.getElementById('submitBtn');
            if (data.available) {
                resultDiv.innerHTML = '<div class="alert alert-success">예약 가능한 시간입니다!</div>';
                submitBtn.disabled = false;
            } else {
                resultDiv.innerHTML = '<div class="alert alert-danger">해당 시간은 예약이 꽉 찼습니다.</div>';
                submitBtn.disabled = true;
            }
        })
        .catch(err => console.error('AJAX 오류:', err));
}

function validateForm() {
    const date      = document.getElementById('dateInput').value;
    const partySize = document.querySelector('input[name=partySize]').value;
    const today     = new Date().toISOString().split('T')[0];

    if (!date) { alert('날짜를 선택해 주세요.'); return false; }
    if (date < today) { alert('과거 날짜는 예약할 수 없습니다.'); return false; }
    if (!partySize || partySize < 1) { alert('인원을 1명 이상 입력해 주세요.'); return false; }
    return true;
}
</script>

</body>
</html>