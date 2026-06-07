<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.RestaurantDAO, dao.MenuDAO" %>
<%@ page import="dto.RestaurantDTO, dto.MenuDTO, java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    RestaurantDTO restaurant = RestaurantDAO.getInstance().getRestaurantById(id);
    ArrayList<MenuDTO> menus = MenuDAO.getInstance().getMenusByRestaurantId(id);
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
	<!-- 식당 정보 -->
	<div class="container mt-4">
    <img src="resources/images/<%= restaurant.getImageFilename() %>" class="restaurant-img w-100 rounded-3">
    <h2><%= restaurant.getName() %></h2>
    <p>카테고리: <%= restaurant.getCategory() %></p>
    <p>주소: <%= restaurant.getAddress() %></p>
    <p>전화: <%= restaurant.getPhone() %></p>
    <p>영업시간: <%= restaurant.getOpeningHours() %></p>
    </div>
    <!-- 메뉴 목록 -->
    <div class="container mt-4">
    <h3>메뉴</h3>
    <div class="row">
        <% for(MenuDTO menu : menus) { %>
        <div class="col-md-3 mb-3">
            <div class="card">
                <img src="resources/images/<%= menu.getImageFilename() %>" class="menu-img card-img-top">
                <div class="card-body">
                    <h6 class="card-title"><%= menu.getName() %></h6>
                    <p class="card-text"><%= menu.getPrice() %>원</p>
                    <!-- 설명이 null이면 빈 문자열로 처리 -->
                    <p class="card-text"><%= menu.getDescription()  != null ? menu.getDescription() : ""%></p>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>
	<!-- 예약 -->
	<div class="container mt-4">
    <h3>예약 신청</h3>
    <form action="reservationForm.jsp" method="get">
        <input type="hidden" name="restaurantId" value="<%= restaurant.getId() %>">
        <div class="mb-3">
            <label>날짜</label>
            <input type="date" name="date" class="form-control" min="<%= java.time.LocalDate.now() %>">
        </div>
        <div class="mb-3">
            <label>시간</label>
            <%
    String[] timeRange = restaurant.getOpeningHours().split(" - ");
    int startHour = Integer.parseInt(timeRange[0].split(":")[0]);
    int startMinute = Integer.parseInt(timeRange[0].split(":")[1]);
    int endHour = Integer.parseInt(timeRange[1].split(":")[0]);

    // 분이 있으면 다음 정시부터 (ex: 11:30 → 12:00)
    if (startMinute > 0) startHour++;
%>
<select name="time" class="form-control">
    <% for (int h = startHour; h < endHour; h++) { %>
    <option><%= String.format("%02d:00", h) %></option>
    <% } %>
</select>
        </div>
        <div class="mb-3">
            <label>인원</label>
            <input type="number" name="partySize" min="1" class="form-control">
        </div>
        <button type="submit" class="btn btn-primary">예약 신청</button>
    </form>
</div>
	


</body>
</html>