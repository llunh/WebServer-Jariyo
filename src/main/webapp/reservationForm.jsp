<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="dao.ReservationDAO" %>
<%@ page import="dto.ReservationDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="./resources/css/bootstrap.min.css" rel="stylesheet">
<title>예약</title>
</head>
<%
    /* restaurantDetail.jsp form에서 전달받은 값 */
    int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
    String date = request.getParameter("date");
    String time = request.getParameter("time");
    int partySize = Integer.parseInt(request.getParameter("partySize"));

    /* DTO에 값 세팅 후 DAO로 INSERT */
    ReservationDTO r = new ReservationDTO();
    r.setRestaurantId(restaurantId);
    r.setReservationDate(date);
    r.setReservationTime(time);
    r.setPartySize(partySize);

    int result = ReservationDAO.getInstance().insertReservation(r);
%>
<body>
<div class="container mt-4">
    <% if (result == 1) { %>
        <div class="alert alert-success">예약이 완료되었습니다.</div>
        <p>날짜: <%= date %></p>
        <p>시간: <%= time %></p>
        <p>인원: <%= partySize %>명</p>
        <a href="restaurants.jsp" class="btn btn-primary">홈으로</a>
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