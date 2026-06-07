<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="mvc.model.RestaurantDTO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String sessionId = (String) session.getAttribute("sessionId");
    List<RestaurantDTO> restaurantList =
        (List<RestaurantDTO>) request.getAttribute("restaurantList");
    boolean hasVisited = (restaurantList != null && !restaurantList.isEmpty());
%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<title>자리요 - 리뷰 작성</title>
<script type="text/javascript">
    function checkForm() {
        if (!document.writeForm.restaurantId.value) {
            alert("식당을 선택해 주세요.");
            return false;
        }
        if (!document.writeForm.content.value.trim()) {
            alert("리뷰 내용을 입력해 주세요.");
            return false;
        }
        if (document.writeForm.content.value.trim().length > 1000) {
            alert("리뷰 내용은 1000자 이내로 작성해 주세요.");
            return false;
        }
    }

    function previewImages(input) {
        var preview = document.getElementById("preview");
        preview.innerHTML = "";
        for (var i = 0; i < input.files.length; i++) {
            var reader = new FileReader();
            reader.onload = function(e) {
                var img = document.createElement("img");
                img.src = e.target.result;
                img.style = "max-width:150px; max-height:150px; margin:4px; object-fit:cover; border-radius:4px;";
                preview.appendChild(img);
            };
            reader.readAsDataURL(input.files[i]);
        }
    }
</script>
</head>
<body>
<div class="container py-4">
    <jsp:include page="/views/menu.jsp" />

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">리뷰 작성</h1>
            <p class="col-md-8 fs-4">Write Review</p>
        </div>
    </div>

    <div class="row justify-content-center">
      <div class="col-md-8">

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <% if (!hasVisited) { %>
        <div class="alert alert-info">
            방문한 식당이 없습니다.<br>
            예약 시간이 지난 후에 해당 식당에 리뷰를 작성할 수 있습니다.
        </div>
        <a href="<c:url value='/ReviewListAction.do?pageNum=1'/>"
           class="btn btn-outline-secondary">목록으로</a>
        <% } else { %>

        <form name="writeForm"
              action="<c:url value='/ReviewWriteAction.do'/>"
              method="post"
              enctype="multipart/form-data"
              onsubmit="return checkForm()">

            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">식당 선택</label>
                <div class="col-sm-9">
                    <select name="restaurantId" class="form-select">
                        <option value="">-- 식당을 선택해 주세요 --</option>
                        <% for (RestaurantDTO r : restaurantList) { %>
                            <option value="<%=r.getId()%>">
                                <%=r.getName()%> (<%=r.getCategory()%>)
                            </option>
                        <% } %>
                    </select>
                </div>
            </div>

            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">별점</label>
                <div class="col-sm-9">
                    <select name="rating" class="form-select">
                        <option value="5">⭐⭐⭐⭐⭐ 5점 - 최고예요!</option>
                        <option value="4">⭐⭐⭐⭐ 4점 - 좋아요</option>
                        <option value="3">⭐⭐⭐ 3점 - 보통이에요</option>
                        <option value="2">⭐⭐ 2점 - 별로예요</option>
                        <option value="1">⭐ 1점 - 최악이에요</option>
                    </select>
                </div>
            </div>

            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">리뷰 내용</label>
                <div class="col-sm-9">
                    <textarea name="content" class="form-control" rows="5"
                              maxlength="1000"
                              placeholder="식당 방문 후기를 작성해 주세요. (최대 1000자)"></textarea>
                </div>
            </div>

            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">사진 첨부</label>
                <div class="col-sm-9">
                    <input type="file" name="images" class="form-control"
                           accept="image/*" multiple
                           onchange="previewImages(this)">
                    <div class="form-text text-muted">jpg, png, gif, webp / 1개당 최대 5MB</div>
                    <div id="preview" class="mt-2"></div>
                </div>
            </div>

            <div class="mb-3 row">
                <div class="col-sm-9 d-flex gap-2">
                    <input type="submit" class="btn btn-primary"   value="리뷰 등록">
                    <input type="reset"  class="btn btn-secondary" value="초기화">
                    <a href="<c:url value='/ReviewListAction.do?pageNum=1'/>"
                       class="btn btn-outline-secondary">목록으로</a>
                </div>
            </div>
        </form>
        <% } %>
      </div>
    </div>
</div>
</body>
</html>