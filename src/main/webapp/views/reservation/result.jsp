<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    int    result    = (Integer) request.getAttribute("result");
    String date      = (String)  request.getAttribute("date");
    String time      = (String)  request.getAttribute("time");
    int    partySize = (Integer) request.getAttribute("partySize");
%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>예약 결과</title>
</head>
<body>
<div class="container mt-4">
    <jsp:include page="/views/menu.jsp" />

    <% if (result == 1) { %>
        <div class="alert alert-success">예약이 완료되었습니다.</div>
        <p>날짜: <%= date %></p>
        <p>시간: <%= time %></p>
        <p>인원: <%= partySize %>명</p>
        <a href="<c:url value='/RestaurantList.do'/>" class="btn btn-primary">식당 목록으로</a>

    <% } else if (result == 0) { %>
        <div class="alert alert-warning">해당 시간대는 정원이 꽉 찼습니다. 다른 시간을 선택해주세요.</div>
        <a href="javascript:history.back()" class="btn btn-secondary">돌아가기</a>

    <% } else { %>
        <div class="alert alert-danger">예약에 실패했습니다. 다시 시도해주세요.</div>
        <a href="javascript:history.back()" class="btn btn-secondary">돌아가기</a>
    <% } %>
</div>
</body>
</html>
