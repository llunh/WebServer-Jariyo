<%@ page contentType="text/html; charset=utf-8" errorPage="/views/error500.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>자리요 - 회원가입</title>
<script type="text/javascript">
    function checkForm() {
        if (document.registerForm.username.value.trim().length < 3) {
            alert("아이디는 3자 이상이어야 합니다.");
            return false;
        }
        if (document.registerForm.password.value.length < 6) {
            alert("비밀번호는 6자 이상이어야 합니다.");
            return false;
        }
        if (document.registerForm.password.value !== document.registerForm.password2.value) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }
        if (!document.registerForm.email.value.trim()) {
            alert("이메일을 입력해 주세요.");
            return false;
        }
        if (!document.registerForm.nickname.value.trim()) {
            alert("닉네임을 입력해 주세요.");
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
            <h1 class="display-5 fw-bold">회원가입</h1>
            <p class="col-md-8 fs-4">Register</p>
        </div>
    </div>

    <div class="row justify-content-center">
      <div class="col-md-6">

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form name="registerForm"
              action="<c:url value='/MemberRegisterAction.do'/>"
              method="post"
              onsubmit="return checkForm()">

            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">아이디</label>
                <div class="col-sm-9">
                    <input name="username" type="text" class="form-control"
                           value="${username}" placeholder="3자 이상">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">비밀번호</label>
                <div class="col-sm-9">
                    <input name="password" type="password" class="form-control"
                           placeholder="6자 이상">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">비밀번호 확인</label>
                <div class="col-sm-9">
                    <input name="password2" type="password" class="form-control"
                           placeholder="비밀번호 재입력">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">이메일</label>
                <div class="col-sm-9">
                    <input name="email" type="email" class="form-control"
                           value="${email}" placeholder="example@email.com">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">닉네임</label>
                <div class="col-sm-9">
                    <input name="nickname" type="text" class="form-control"
                           value="${nickname}" placeholder="서비스에서 사용할 이름">
                </div>
            </div>
            <div class="mb-3 row">
                <div class="col-sm-9 d-flex gap-2">
                    <input type="submit" class="btn btn-primary"   value="가입하기">
                    <input type="reset"  class="btn btn-secondary" value="초기화">
                </div>
            </div>
        </form>

        <hr>
        <p class="text-center">
            이미 계정이 있으신가요?
            <a href="<c:url value='/MemberLoginForm.do'/>">로그인</a>
        </p>
      </div>
    </div>
</div>
</body>
</html>