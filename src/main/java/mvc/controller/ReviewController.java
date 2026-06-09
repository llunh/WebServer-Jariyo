package mvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import mvc.model.ReviewDAO;
import mvc.model.ReviewDTO;
import mvc.model.ReviewImageDTO;
import mvc.model.ReviewLikeDAO;
import mvc.model.UserDTO;
import mvc.model.RestaurantDAO;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize       = 5  * 1024 * 1024,
    maxRequestSize    = 20 * 1024 * 1024
)
public class ReviewController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final int LISTCOUNT = 5;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String RequestURI  = request.getRequestURI();
        String contextPath = request.getContextPath();
        String command     = RequestURI.substring(contextPath.length());

        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("utf-8");

        if (command.equals("/ReviewListAction.do")) {
            requestReviewList(request);
            RequestDispatcher rd = request.getRequestDispatcher("/views/review/list.jsp");
            rd.forward(request, response);

        } else if (command.equals("/ReviewWriteForm.do")) {
            UserDTO loginUser = (UserDTO) request.getSession(false).getAttribute("loginUser");

            request.setAttribute("restaurantList",
                RestaurantDAO.getInstance().getVisitedRestaurants(loginUser.getId()));

            RequestDispatcher rd = request.getRequestDispatcher("/views/review/writeForm.jsp");
            rd.forward(request, response);

        } else if (command.equals("/ReviewWriteAction.do")) {
            requestReviewWrite(request, response);

        } else if (command.equals("/ReviewDeleteAction.do")) {
            requestReviewDelete(request, response);

        } else if (command.equals("/ReviewLikeAction.do")) {
            requestReviewLike(request, response);
        }
    }

    // 리뷰 목록 가져오기
    public void requestReviewList(HttpServletRequest request) {
        ReviewDAO dao     = ReviewDAO.getInstance();
        int       pageNum = 1;
        int       limit   = LISTCOUNT;

        if (request.getParameter("pageNum") != null)
            pageNum = Integer.parseInt(request.getParameter("pageNum"));

        HttpSession session = request.getSession();
        String lang = request.getParameter("lang");
        if (lang != null && !lang.isEmpty()) {
            session.setAttribute("lang", lang);
        } else {
            lang = (String) session.getAttribute("lang");
            if (lang == null) lang = "ko";
        }

        int                  total_record = dao.getReviewCount();
        ArrayList<ReviewDTO> reviewList   = dao.getReviewList(pageNum, limit);

        int total_page;
        if (total_record % limit == 0)
            total_page = total_record / limit;
        else
            total_page = total_record / limit + 1;

        request.setAttribute("pageNum",      pageNum);
        request.setAttribute("total_page",   total_page);
        request.setAttribute("total_record", total_record);
        request.setAttribute("reviewList",   reviewList);
        request.setAttribute("lang",         lang);
    }

    // 리뷰 저장 처리
    public void requestReviewWrite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session   = request.getSession(false);
        UserDTO     loginUser = (UserDTO) session.getAttribute("loginUser");

        String content         = request.getParameter("content").trim();
        String restaurantIdStr = request.getParameter("restaurantId");
        String ratingStr       = request.getParameter("rating");

        // 식당 선택 여부 확인
        if (restaurantIdStr == null || restaurantIdStr.isEmpty()) {
            request.setAttribute("error", "식당을 선택해 주세요.");

            request.setAttribute("restaurantList",
                RestaurantDAO.getInstance().getVisitedRestaurants(loginUser.getId()));

            RequestDispatcher rd = request.getRequestDispatcher("/views/review/writeForm.jsp");
            rd.forward(request, response);
            return;
        }

        int restaurantId = Integer.parseInt(restaurantIdStr);


        // 방문 여부 확인 - 예약한 식당만 리뷰 작성 가능
        if (!RestaurantDAO.getInstance().hasVisited(loginUser.getId(), restaurantId)) {
            request.setAttribute("error", "예약 후 방문한 식당에만 리뷰를 작성할 수 있습니다.");
            request.setAttribute("restaurantList",
                RestaurantDAO.getInstance().getVisitedRestaurants(loginUser.getId()));

            RequestDispatcher rd = request.getRequestDispatcher("/views/review/writeForm.jsp");
            rd.forward(request, response);
            return;
        }

        if (content.isEmpty() || content.length() > 1000) {
            request.setAttribute("error", "리뷰 내용을 1~1000자로 입력해 주세요.");

            request.setAttribute("restaurantList",
                RestaurantDAO.getInstance().getVisitedRestaurants(loginUser.getId()));

            RequestDispatcher rd = request.getRequestDispatcher("/views/review/writeForm.jsp");
            rd.forward(request, response);
            return;
        }

        String uploadDir = getServletContext().getRealPath("/uploads");
        File   dir       = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        List<String>              allowedExt = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");
        ArrayList<ReviewImageDTO> images     = new ArrayList<ReviewImageDTO>();
        Collection<Part>          parts      = request.getParts();

        for (Part part : parts) {
            if (!"images".equals(part.getName()) || part.getSize() == 0) continue;
            String submitted = part.getSubmittedFileName();
            if (submitted == null || submitted.isEmpty()) continue;

            String oriName = new File(submitted).getName();
            int    dotIdx  = oriName.lastIndexOf('.');
            String ext     = (dotIdx >= 0) ? oriName.substring(dotIdx).toLowerCase() : "";

            if (!allowedExt.contains(ext)) {
                request.setAttribute("error", "이미지 파일(jpg, png, gif, webp)만 업로드 가능합니다.");

                request.setAttribute("restaurantList",
                    RestaurantDAO.getInstance().getVisitedRestaurants(loginUser.getId()));

                RequestDispatcher rd = request.getRequestDispatcher("/views/review/writeForm.jsp");
                rd.forward(request, response);
                return;
            }

            String savedName = java.util.UUID.randomUUID().toString() + ext;
            part.write(uploadDir + File.separator + savedName);

            ReviewImageDTO img = new ReviewImageDTO();
            img.setFileName(savedName);
            img.setOriName(oriName);
            images.add(img);
        }

        ReviewDTO review = new ReviewDTO();
        review.setUserId(loginUser.getId());
        review.setRestaurantId(restaurantId);
        review.setRating(Integer.parseInt(ratingStr));
        review.setContent(content);

        ReviewDAO dao    = ReviewDAO.getInstance();
        boolean   result = dao.insertReview(review, images);

        if (result) {
            response.sendRedirect(request.getContextPath() + "/ReviewListAction.do?pageNum=1");
        } else {
            request.setAttribute("error", "리뷰 저장에 실패했습니다. 다시 시도해 주세요.");

            request.setAttribute("restaurantList",
                RestaurantDAO.getInstance().getVisitedRestaurants(loginUser.getId()));

            RequestDispatcher rd = request.getRequestDispatcher("/views/review/writeForm.jsp");
            rd.forward(request, response);
        }
    }

    // 리뷰 삭제 처리
    public void requestReviewDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session   = request.getSession(false);
        UserDTO     loginUser = (UserDTO) session.getAttribute("loginUser");

        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        ReviewDAO dao    = ReviewDAO.getInstance();
        boolean   result = dao.deleteReview(reviewId, loginUser.getId());

        if (result) {
            response.sendRedirect(request.getContextPath() + "/ReviewListAction.do?pageNum=1");
        } else {
            response.sendRedirect(request.getContextPath() + "/ReviewListAction.do?pageNum=1&error=delete");
        }
    }

    // 좋아요 처리
    public void requestReviewLike(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session   = request.getSession(false);
        UserDTO     loginUser = (UserDTO) session.getAttribute("loginUser");

        int reviewId = Integer.parseInt(request.getParameter("reviewId"));
        int pageNum  = Integer.parseInt(request.getParameter("pageNum"));

        ReviewLikeDAO likeDAO = ReviewLikeDAO.getInstance();

        if (likeDAO.isLiked(reviewId, loginUser.getId())) {
            likeDAO.removeLike(reviewId, loginUser.getId());
        } else {
            likeDAO.addLike(reviewId, loginUser.getId());
        }

        response.sendRedirect(request.getContextPath()
                + "/ReviewListAction.do?pageNum=" + pageNum);
    }
}