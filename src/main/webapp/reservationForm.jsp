<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="mvc.model.ReservationDAO"%>
<%@ page import="mvc.model.ReservationDTO"%>
<%@ page import="mvc.model.UserDTO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="./resources/css/bootstrap.min.css" rel="stylesheet">
<title>예약 확인</title>
</head>
<%
    int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
    String date      = request.getParameter("date");
    String time      = request.getParameter("time");
    int partySize    = Integer.parseInt(request.getParameter("partySize"));

    // 과거 날짜 서버 사이드 유효성 검사
    String today = java.time.LocalDate.now().toString();
    if (date.compareTo(today) < 0) {
        response.sendRedirect("restaurantDetail.jsp?id=" + restaurantId + "&error=date");
        return;
    }

    ReservationDTO r = new ReservationDTO();
    r.setRestaurantId(restaurantId);
    r.setReservationDate(date);
    r.setReservationTime(time);
    r.setPartySize(partySize);

    // user_id 저장
    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
    if (loginUser != null) {
        r.setUserId(loginUser.getId());
    }

    int result = ReservationDAO.getInstance().insertReservation(r);
%>

<%-- jsp:useBean으로 예약 정보 표시 --%>
<jsp:useBean id="reservation" class="mvc.model.ReservationDTO" scope="request"/>
<jsp:setProperty name="reservation" property="reservationDate" value="<%=date%>"/>
<jsp:setProperty name="reservation" property="reservationTime" value="<%=time%>"/>
<jsp:setProperty name="reservation" property="partySize" value="<%=partySize%>"/>

<body>
<div class="container mt-4">
    <% if (result == 1) { %>
        <div class="alert alert-success">예약이 완료되었습니다!</div>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">예약 정보</h5>
                <p><strong>날짜:</strong>
                    <jsp:getProperty name="reservation" property="reservationDate"/></p>
                <p><strong>시간:</strong>
                    <jsp:getProperty name="reservation" property="reservationTime"/></p>
                <p><strong>인원:</strong>
                    <jsp:getProperty name="reservation" property="partySize"/>명</p>
            </div>
        </div>
        <a href="restaurants.jsp" class="btn btn-primary mt-3">홈으로</a>
        <a href="<%=request.getContextPath()%>/MemberMyPage.do"
           class="btn btn-secondary mt-3">내 예약 확인</a>
    <% } else if (result == 0) { %>
        <div class="alert alert-warning">해당 시간대는 정원이 꽉 찼습니다.</div>
        <a href="javascript:history.back()" class="btn btn-secondary">돌아가기</a>
    <% } else if (result == 2) { %>
        <%-- 같은 날짜에 이미 예약이 존재하는 경우 --%>
        <div class="alert alert-warning">이미 해당 날짜에 예약이 있습니다. 다른 날짜를 선택해주세요.</div>
        <a href="javascript:history.back()" class="btn btn-secondary">돌아가기</a>
    <% } else { %>
        <div class="alert alert-danger">예약에 실패했습니다. 다시 시도해주세요.</div>
        <a href="javascript:history.back()" class="btn btn-secondary">돌아가기</a>
    <% } %>
</div>
</body>
</html>