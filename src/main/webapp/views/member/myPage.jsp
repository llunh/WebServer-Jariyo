<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="mvc.model.UserDTO"%>
<%@ page import="dto.ReservationDTO, java.util.ArrayList"%>
<%
    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
    ArrayList<ReservationDTO> reservations = (ArrayList<ReservationDTO>) request.getAttribute("reservations");
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/MemberLoginForm.do");
        return;
    }
%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>자리요 - 마이페이지</title>
</head>
<body>
<div class="container py-4">
    <jsp:include page="/views/menu.jsp" />

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">마이페이지</h1>
            <p class="col-md-8 fs-4">My Page</p>
        </div>
    </div>

    <div class="row justify-content-center">
      <div class="col-md-6">

        <%-- 회원 정보 --%>
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title mb-3">회원 정보</h5>
                <p><strong>아이디:</strong> <%=loginUser.getUsername()%></p>
                <p><strong>닉네임:</strong> <%=loginUser.getNickname()%></p>
                <p><strong>이메일:</strong> <%=loginUser.getEmail()%></p>
                <p><strong>가입일:</strong> <%=loginUser.getCreatedAt()%></p>
            </div>
        </div>

        <%-- 예약 내역 --%>
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title mb-3">예약 내역</h5>
                <% if (reservations == null || reservations.isEmpty()) { %>
                    <p class="text-muted">예약 내역이 없습니다.</p>
                <% } else { %>
                    <table class="table table-bordered">
                        <thead class="table-light">
                            <tr>
                                <th>식당</th>
                                <th>날짜</th>
                                <th>시간</th>
                                <th>인원</th>
                                <th>상태</th>
                                <th>예약취소</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (ReservationDTO r : reservations) { %>
                            <tr>
                                <td><%= r.getRestaurantName() %></td>
                                <td><%= r.getReservationDate() %></td>
                                <td><%= r.getReservationTime() %></td>
                                <td><%= r.getPartySize() %>명</td>
                                <td><%= "CONFIRMED".equals(r.getStatus()) ? "예약확정" : "취소됨" %></td>
                                <td>
                                    <% if ("CONFIRMED".equals(r.getStatus())) { %>
                                    <a href="<%= request.getContextPath() %>/ReservationCancelAction.do?reservationId=<%= r.getId() %>"
                                       class="btn btn-sm btn-danger"
                                       onclick="return confirm('예약을 취소하시겠습니까?')">취소</a>
                                    <% } %>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
            </div>
        </div>

        <%-- 회원 탈퇴 --%>
        <div class="card border-danger">
            <div class="card-body">
                <h5 class="card-title text-danger mb-3">회원 탈퇴</h5>
                <p class="text-muted">탈퇴 시 작성한 모든 리뷰가 삭제됩니다.</p>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form name="deleteForm"
                      action="<c:url value='/MemberDeleteAction.do'/>"
                      method="post"
                      onsubmit="return confirm('정말 탈퇴하시겠습니까? 모든 리뷰가 삭제됩니다.')">

                    <div class="mb-3 row">
                        <label class="col-sm-4 col-form-label">비밀번호 확인</label>
                        <div class="col-sm-8">
                            <input name="password" type="password" class="form-control"
                                   placeholder="비밀번호를 입력해 주세요" required>
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <input type="submit" class="btn btn-danger" value="회원 탈퇴">
                        <a href="<c:url value='/ReviewListAction.do?pageNum=1'/>"
                           class="btn btn-secondary">취소</a>
                    </div>
                </form>
            </div>
        </div>

      </div>
    </div>
</div>
</body>
</html>