<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="dto.RestaurantDTO, java.util.ArrayList" %>
<%
    ArrayList<RestaurantDTO> list = (ArrayList<RestaurantDTO>) request.getAttribute("restaurantList");
%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>자리요 - 식당 목록</title>
<style>
    .card-img-top { height: 300px; object-fit: cover; }
</style>
<script>
let selectedCategory = '전체';

function filterCards() {
    const keyword = document.getElementById("searchInput").value.toLowerCase();
    const cards   = document.querySelectorAll("#cardContainer > div");

    cards.forEach(card => {
        const name     = card.dataset.name.toLowerCase();
        const category = card.dataset.category;

        const matchKeyword  = name.includes(keyword);
        const matchCategory = selectedCategory === '전체' || category === selectedCategory;

        card.style.display = (matchKeyword && matchCategory) ? "" : "none";
    });
}

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
</head>
<body>
<div class="container mt-4">
    <jsp:include page="/views/menu.jsp" />

    <h2>식당 목록</h2>

    <!-- 검색창 -->
    <div class="input-group mb-3">
        <input type="text" id="searchInput" class="form-control"
               placeholder="식당 이름 검색...">
        <button class="btn btn-outline-secondary" onclick="filterCards()">🔍</button>
    </div>

    <!-- 카테고리 필터 -->
    <div class="mb-4" id="categoryFilter">
        <button class="btn btn-primary" onclick="setCategory('전체')">전체</button>
        <%
            java.util.Set<String> categories = new java.util.LinkedHashSet<>();
            for (RestaurantDTO r : list) categories.add(r.getCategory());
            for (String cat : categories) {
        %>
        <button class="btn btn-outline-primary" onclick="setCategory('<%= cat %>')"><%= cat %></button>
        <% } %>
    </div>

    <!-- 식당 카드 목록 -->
    <div class="row" id="cardContainer">
        <% for (RestaurantDTO r : list) { %>
        <div class="col-md-4 mb-3" data-name="<%= r.getName() %>" data-category="<%= r.getCategory() %>">
            <div class="card">
                <img src="<%= request.getContextPath() %>/resources/images/<%= r.getImageFilename() %>" class="card-img-top">
                <div class="card-body">
                    <h5 class="card-title"><%= r.getName() %></h5>
                    <p class="card-text"><%= r.getCategory() %></p>
                    <p class="card-text"><%= r.getAddress() %></p>
                    <a href="<c:url value='/RestaurantDetail.do'/>?id=<%= r.getId() %>" class="btn btn-primary">상세보기</a>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>
