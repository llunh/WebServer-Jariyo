<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String sessionId = (String) session.getAttribute("sessionId");
    String nickname  = (String) session.getAttribute("nickname");
%>

<header class="pb-3 mb-4 border-bottom">
  <div class="container">
    <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">

      <a href="<c:url value='/ReviewListAction.do?pageNum=1'/>"
         class="d-flex align-items-center text-dark text-decoration-none me-3">
        <span class="fs-4">🍽 자리요</span>
      </a>

      <ul class="nav nav-pills">
        <c:choose>
          <c:when test="${empty sessionId}">
            <li class="nav-item">
              <a class="nav-link" href="<c:url value='/MemberLoginForm.do'/>">로그인</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value='/MemberRegisterForm.do'/>">회원가입</a>
            </li>
          </c:when>
          <c:otherwise>
            <li class="nav-item d-flex align-items-center px-2">
              <span class="text-muted">[<%=nickname%>님]</span>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value='/MemberMyPage.do'/>">마이페이지</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value='/MemberLogoutAction.do'/>">로그아웃</a>
            </li>
          </c:otherwise>
        </c:choose>

        <li class="nav-item">
          <a class="nav-link" href="<c:url value='/ReviewListAction.do?pageNum=1'/>">리뷰 목록</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<c:url value='/ReviewWriteForm.do'/>">리뷰 작성</a>
        </li>
      </ul>
    </div>
  </div>
</header>