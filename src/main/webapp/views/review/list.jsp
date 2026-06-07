<%@ page contentType="text/html; charset=utf-8" errorPage="/views/error500.jsp"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.*"%>
<%@ page import="mvc.model.ReviewDTO"%>
<%@ page import="mvc.model.ReviewImageDTO"%>
<%@ page import="mvc.model.ReviewLikeDAO"%>
<%@ page import="mvc.model.UserDTO"%>

<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="bundle.messages" />

<%
    String  sessionId    = (String)  session.getAttribute("sessionId");
    UserDTO loginUser    = (UserDTO) session.getAttribute("loginUser");
    List    reviewList   = (List)    request.getAttribute("reviewList");
    int     total_record = ((Integer) request.getAttribute("total_record")).intValue();
    int     pageNum      = ((Integer) request.getAttribute("pageNum")).intValue();
    int     total_page   = ((Integer) request.getAttribute("total_page")).intValue();
%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title><fmt:message key="review.list.title"/></title>
<script type="text/javascript">
    function checkLogin() {
        if ('<%=sessionId%>' === 'null' || '<%=sessionId%>' === '') {
            alert('<fmt:message key="review.login.required"/>');
            location.href = '<c:url value="/MemberLoginForm.do"/>';
            return false;
        }
        location.href = '<c:url value="/ReviewWriteForm.do"/>';
    }
</script>
</head>
<body>
<div class="container py-4">
    <jsp:include page="/views/menu.jsp" />

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold"><fmt:message key="review.list.heading"/></h1>
            <p class="col-md-8 fs-4">Zariyo Reviews</p>
        </div>
    </div>

    <%-- 언어 전환 --%>
    <div class="text-end mb-2">
        <a href="?pageNum=<%=pageNum%>&lang=ko" class="btn btn-sm btn-outline-secondary">한국어</a>
        <a href="?pageNum=<%=pageNum%>&lang=en" class="btn btn-sm btn-outline-secondary">English</a>
    </div>

    <div class="text-end mb-2">
        <span class="badge text-bg-success">
            <fmt:message key="review.total"/> <%=total_record%>건
        </span>
    </div>

    <%
        if (reviewList == null || reviewList.isEmpty()) {
    %>
        <div class="alert alert-info"><fmt:message key="review.empty"/></div>
    <%
        } else {
            for (int j = 0; j < reviewList.size(); j++) {
                ReviewDTO review = (ReviewDTO) reviewList.get(j);
    %>
        <div class="card mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <h5 class="card-title">
                        🍽 <%=review.getRestaurantName()%>
                        &nbsp;
                        <% for (int s = 0; s < review.getRating(); s++) { out.print("⭐"); } %>
                        <small class="text-muted">(<%=review.getRating()%>/5)</small>
                    </h5>
                    <small class="text-muted"><%=review.getCreatedAt()%></small>
                </div>
                <p class="text-muted mb-2">
                    <fmt:message key="review.by"/> <%=review.getUsername()%>
                </p>
                <p class="card-text"><%=review.getContent()%></p>

                <%-- 이미지 출력 --%>
                <%
                    List<ReviewImageDTO> imgs = review.getImages();
                    if (imgs != null && !imgs.isEmpty()) {
                        for (ReviewImageDTO img : imgs) {
                %>
                    <img src="<%=request.getContextPath()%>/uploads/<%=img.getFileName()%>"
                         alt="<%=img.getOriName()%>"
                         style="max-width:200px; max-height:200px; margin:4px; object-fit:cover;"
                         class="rounded">
                <%
                        }
                    }
                %>

                <%-- 좋아요 버튼 --%>
                <div class="mt-2 d-flex align-items-center gap-2">
                    <%
                        boolean liked = false;
                        if (loginUser != null) {
                            liked = ReviewLikeDAO.getInstance().isLiked(review.getId(), loginUser.getId());
                        }
                        String likeClass = liked ? "btn-danger" : "btn-outline-danger";
                        String likeText  = liked ? "좋아요 취소" : "좋아요";
                    %>
                    <%
                        if (loginUser != null) {
                    %>
                        <a href="<%=request.getContextPath()%>/ReviewLikeAction.do?reviewId=<%=review.getId()%>&pageNum=<%=pageNum%>"
                           class="btn btn-sm <%=likeClass%>">
                            &#10084; <%=likeText%> (<%=review.getLikeCount()%>)
                        </a>
                    <%
                        } else {
                    %>
                        <span class="btn btn-sm btn-outline-danger disabled">
                            &#10084; 좋아요 (<%=review.getLikeCount()%>)
                        </span>
                    <%
                        }
                    %>
                </div>

                <%-- 본인 리뷰일 때만 삭제 버튼 표시 --%>
                <%
                    if (loginUser != null && loginUser.getId() == review.getUserId()) {
                %>
                    <div class="mt-2">
                        <a href="<%=request.getContextPath()%>/ReviewDeleteAction.do?reviewId=<%=review.getId()%>"
                           class="btn btn-sm btn-danger"
                           onclick="return confirm('정말 삭제하시겠습니까?')">
                            삭제
                        </a>
                    </div>
                <%
                    }
                %>
            </div>
        </div>
    <%
            }
        }
    %>

    <%-- 페이지 네비게이션 --%>
    <div align="center" class="my-3">
        <c:set var="pageNum" value="<%=pageNum%>" />
        <c:forEach var="i" begin="1" end="<%=total_page%>">
            <a href="<c:url value='/ReviewListAction.do?pageNum=${i}'/>">
                <c:choose>
                    <c:when test="${pageNum == i}"><b>[${i}]</b></c:when>
                    <c:otherwise>[${i}]</c:otherwise>
                </c:choose>
            </a>
        </c:forEach>
    </div>

    <div class="py-3 text-end">
        <a href="#" onclick="checkLogin(); return false;" class="btn btn-primary">
            &laquo; <fmt:message key="review.write.btn"/>
        </a>
    </div>
</div>
</body>
</html>