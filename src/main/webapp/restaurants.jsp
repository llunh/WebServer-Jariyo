<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.RestaurantDAO" %>
<%@ page import="dao.MenuDAO" %>
<%@ page import="dto.RestaurantDTO" %>
<%@ page import="dto.MenuDTO" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="./resources/css/bootstrap.min.css" rel="stylesheet">
<title>메인화면</title>
</head>
<!-- DAO 호출 -->
<%
    RestaurantDAO dao = RestaurantDAO.getInstance();
    ArrayList<RestaurantDTO> list = dao.getAllRestaurants();
%>

<!-- 이미지 스타일 추가(카드 크기 조정) -->
<style>
    .card-img-top {
        height: 300px;
        object-fit: cover;
    }
</style>
<!-- 자바스크립트 필터함수(검색) -->
<script>
function filterCards() {
	 const keyword = document.getElementById("searchInput").value.toLowerCase();
	    const cards = document.querySelectorAll("#cardContainer > div");

	    cards.forEach(card => {
	        const name = card.dataset.name.toLowerCase();
	        const category = card.dataset.category;

	        const matchKeyword = name.includes(keyword);
	        const matchCategory = selectedCategory === '전체' || category === selectedCategory;

	        card.style.display = (matchKeyword && matchCategory) ? "" : "none";
	    });
}

let selectedCategory = '전체';

function setCategory(cat) {
    selectedCategory = cat;
    document.querySelectorAll("#categoryFilter button").forEach(btn => {
        btn.className = btn.textContent === cat
            ? "btn btn-primary"
            : "btn btn-outline-primary";
    });
    filterCards();
}
</script>
<body>
<!-- 식당 카드 출력 -->
<div class="container mt-4">
    <h2>식당 목록</h2>
    
    <!-- 식당 검색 -->
   <div class="input-group mb-3">
    <input type="text" id="searchInput" class="form-control"
           placeholder="식당 이름 검색...">
    <button class="btn btn-outline-secondary" onclick="filterCards()">🔍</button>
</div>
<div class="mb-4" id="categoryFilter">
    <button class="btn btn-primary" onclick="setCategory('전체')">전체</button>
    <%
        java.util.Set<String> categories = new java.util.LinkedHashSet<>();
        for(RestaurantDTO r : list) categories.add(r.getCategory());
        for(String cat : categories) {
    %>
    <button class="btn btn-outline-primary" onclick="setCategory('<%= cat %>')"><%= cat %></button>
    <% } %>
</div>
       
    <div class="row" id="cardContainer">
    <!-- 식당 카드 출력 -->
        <% for(RestaurantDTO r : list) { %>
        <div class="col-md-4 mb-3"data-name="<%= r.getName() %>" data-category="<%= r.getCategory() %>">
            <div class="card">
                <img src="resources/images/<%= r.getImageFilename() %>" class="card-img-top">
                <div class="card-body">
                    <h5 class="card-title"><%= r.getName() %></h5>
                    <p class="card-text"><%= r.getCategory() %></p>
                    <p class="card-text"><%= r.getAddress() %></p>
                    <a href="restaurantDetail.jsp?id=<%= r.getId() %>" class="btn btn-primary">상세보기</a>
                </div>
            </div>
        </div>
        <% } %>

    </div>
</div>
</body>
</html>