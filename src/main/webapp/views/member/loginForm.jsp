<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String savedUsername = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("savedUsername".equals(c.getName())) {
                savedUsername = c.getValue();
                break;
            }
        }
    }
    request.setAttribute("savedUsername", savedUsername);
%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>자리요 - 로그인</title>
<script type="text/javascript">
    function checkForm() {
        if (!document.loginForm.username.value.trim()) {
            alert("아이디를 입력해 주세요.");
            return false;
        }
        if (!document.loginForm.password.value) {
            alert("비밀번호를 입력해 주세요.");
            return false;
        }
    }
</script>
</head>
<body>
<div class="container py-4">
    <jsp:include page="/views/menu.jsp" />

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">로그인</h1>
            <p class="col-md-8 fs-4">Login</p>
        </div>
    </div>

    <div class="row justify-content-center">
      <div class="col-md-5">

        <c:if test="${param.registered eq 'true'}">
            <div class="alert alert-success">회원가입이 완료되었습니다. 로그인해 주세요.</div>
        </c:if>
        <c:if test="${param.redirected eq 'true'}">
            <div class="alert alert-warning">로그인이 필요한 서비스입니다.</div>
        </c:if>
        <c:if test="${param.deleted eq 'true'}">
            <div class="alert alert-info">회원 탈퇴가 완료되었습니다.</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form name="loginForm"
              action="<c:url value='/MemberLoginAction.do'/>"
              method="post"
              onsubmit="return checkForm()">

            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">아이디</label>
                <div class="col-sm-9">
                    <input name="username" type="text" class="form-control"
                           value="${savedUsername}" placeholder="아이디 입력">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">비밀번호</label>
                <div class="col-sm-9">
                    <input name="password" type="password" class="form-control"
                           placeholder="비밀번호 입력">
                </div>
            </div>
            <div class="mb-3 row">
                <div class="col-sm-9">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox"
                               name="remember" id="remember"
                               <c:if test="${not empty savedUsername}">checked</c:if>>
                        <label class="form-check-label" for="remember">아이디 기억하기</label>
                    </div>
                </div>
            </div>
            <div class="mb-3 row">
                <div class="col-sm-9 d-flex gap-2">
                    <input type="submit" class="btn btn-primary"   value="로그인">
                    <input type="reset"  class="btn btn-secondary" value="초기화">
                </div>
            </div>
        </form>

        <hr>
        <p class="text-center">
            계정이 없으신가요?
            <a href="<c:url value='/MemberRegisterForm.do'/>">회원가입</a>
        </p>
      </div>
    </div>
</div>
</body>
</html>