<%@ page contentType="text/html; charset=utf-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>자리요 - 서버 오류</title>
</head>
<body>
<div class="container py-5 text-center">
    <jsp:include page="/views/menu.jsp" />
    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <h1 class="display-1 fw-bold text-danger">500</h1>
        <p class="fs-3">서버 내부 오류가 발생했습니다.</p>
        <p class="text-muted">잠시 후 다시 시도해 주세요.</p>
        <a href="<c:url value='/ReviewListAction.do?pageNum=1'/>" 
           class="btn btn-primary">홈으로 돌아가기</a>
    </div>
</div>
</body>
</html>