<%@ page contentType="text/html; charset=utf-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>자리요 - 페이지 없음</title>
</head>
<body>
<div class="container py-5 text-center">
    <jsp:include page="/views/menu.jsp" />
    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <h1 class="display-1 fw-bold text-muted">404</h1>
        <p class="fs-3">요청하신 페이지를 찾을 수 없습니다.</p>
        <a href="<c:url value='/ReviewListAction.do?pageNum=1'/>" 
           class="btn btn-primary">홈으로 돌아가기</a>
    </div>
</div>
</body>
</html>