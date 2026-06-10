package mvc.controller;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.model.FavoriteDAO;
import mvc.model.ReservationDAO;
import mvc.model.ReservationDTO;
import mvc.model.RestaurantDTO;
import mvc.model.ReviewDAO;
import mvc.model.ReviewDTO;
import mvc.model.UserDAO;
import mvc.model.UserDTO;

public class MemberController extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        if (command.equals("/MemberRegisterForm.do")) {
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/registerForm.jsp");
            rd.forward(request, response);

        } else if (command.equals("/MemberRegisterAction.do")) {
            requestRegister(request, response);

        } else if (command.equals("/MemberLoginForm.do")) {
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/loginForm.jsp");
            rd.forward(request, response);

        } else if (command.equals("/MemberLoginAction.do")) {
            requestLogin(request, response);

        } else if (command.equals("/MemberLogoutAction.do")) {
            requestLogout(request, response);

        } else if (command.equals("/MemberMyPage.do")) {
            UserDTO loginUser = (UserDTO) request.getSession(false).getAttribute("loginUser");
            if (loginUser != null) {
                ArrayList<ReservationDTO> reservations =
                    ReservationDAO.getInstance().getReservationsByUserId(loginUser.getId());
                request.setAttribute("reservations", reservations);

                ArrayList<RestaurantDTO> favorites =
                    FavoriteDAO.getInstance().getFavoriteRestaurants(loginUser.getId());
                request.setAttribute("favorites", favorites);
                
                // 내 리뷰 조회
                ArrayList<ReviewDTO> myReviews =
                    ReviewDAO.getInstance().getReviewsByUserId(loginUser.getId());
                request.setAttribute("myReviews", myReviews);
            }
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/myPage.jsp");
            rd.forward(request, response);
        } else if (command.equals("/MemberDeleteAction.do")) {
            // 회원 탈퇴 처리
            requestDeleteMember(request, response);
        }
    }

    // 회원가입 처리
    private void requestRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username  = request.getParameter("username").trim();
        String password  = request.getParameter("password").trim();
        String password2 = request.getParameter("password2").trim();
        String email     = request.getParameter("email").trim();
        String nickname  = request.getParameter("nickname").trim();

        UserDAO dao   = UserDAO.getInstance();
        String  error = null;

        if (username.length() < 3) {
            error = "아이디는 3자 이상이어야 합니다.";
        } else if (!password.equals(password2)) {
            error = "비밀번호가 일치하지 않습니다.";
        } else if (password.length() < 6) {
            error = "비밀번호는 6자 이상이어야 합니다.";
        } else if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            error = "이메일 형식이 올바르지 않습니다.";
        } else if (nickname.isEmpty()) {
            error = "닉네임을 입력해 주세요.";
        } else if (dao.existsByUsername(username)) {
            error = "이미 사용 중인 아이디입니다.";
        } else if (dao.existsByEmail(email)) {
            error = "이미 사용 중인 이메일입니다.";
        } else if (dao.existsByNickname(nickname)) {
            error = "이미 사용 중인 닉네임입니다.";
        }

        if (error != null) {
            request.setAttribute("error",    error);
            request.setAttribute("username", username);
            request.setAttribute("email",    email);
            request.setAttribute("nickname", nickname);
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/registerForm.jsp");
            rd.forward(request, response);
            return;
        }

        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setNickname(nickname);

        boolean ok = dao.insertUser(user);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/MemberLoginForm.do?registered=true");
        } else {
            request.setAttribute("error", "회원가입에 실패했습니다. 다시 시도해 주세요.");
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/registerForm.jsp");
            rd.forward(request, response);
        }
    }

    // 로그인 처리
    private void requestLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String remember = request.getParameter("remember");

        UserDAO dao  = UserDAO.getInstance();
        UserDTO user = dao.getUserByUsername(username);

        if (user == null || !dao.checkPassword(password, user.getPassword())) {
            request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/loginForm.jsp");
            rd.forward(request, response);
            return;
        }

        HttpSession old = request.getSession(false);
        if (old != null) old.invalidate();

        HttpSession session = request.getSession(true);
        session.setAttribute("loginUser", user);
        session.setAttribute("sessionId", user.getUsername());
        session.setAttribute("nickname",  user.getNickname());
        session.setMaxInactiveInterval(30 * 60);

        if ("on".equals(remember)) {
            Cookie cookie = new Cookie("savedUsername", username);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("savedUsername", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        response.sendRedirect(request.getContextPath() + "/RestaurantList.do");
    }

    // 로그아웃 처리
    private void requestLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        response.sendRedirect(request.getContextPath() + "/MemberLoginForm.do");
    }

    // 회원 탈퇴 처리
    private void requestDeleteMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        // 비로그인 상태로 직접 접근 시 로그인 페이지로 이동
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/MemberLoginForm.do");
            return;
        }
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/MemberLoginForm.do");
            return;
        }

        String rawPassword = request.getParameter("password");
        // 비밀번호 파라미터가 없으면 마이페이지로 돌아감
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            request.setAttribute("error", "비밀번호를 입력해 주세요.");
            request.setAttribute("reservations", ReservationDAO.getInstance().getReservationsByUserId(loginUser.getId()));
            request.setAttribute("favorites",    FavoriteDAO.getInstance().getFavoriteRestaurants(loginUser.getId()));
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/myPage.jsp");
            rd.forward(request, response);
            return;
        }
        String password = rawPassword.trim();

        UserDAO dao = UserDAO.getInstance();

        // 비밀번호 확인
        if (!dao.checkPassword(password, loginUser.getPassword())) {
            // 비밀번호 오류 시 마이페이지에 필요한 데이터를 다시 세팅 후 forward
            request.setAttribute("reservations", ReservationDAO.getInstance().getReservationsByUserId(loginUser.getId()));
            request.setAttribute("favorites",    FavoriteDAO.getInstance().getFavoriteRestaurants(loginUser.getId()));
            request.setAttribute("error", "비밀번호가 올바르지 않습니다.");
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/myPage.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            boolean ok = dao.deleteUser(loginUser.getId());
            if (ok) {
                // 탈퇴 성공 → 세션 삭제 후 로그인 페이지로
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/MemberLoginForm.do?deleted=true");
            } else {
                request.setAttribute("reservations", ReservationDAO.getInstance().getReservationsByUserId(loginUser.getId()));
                request.setAttribute("favorites",    FavoriteDAO.getInstance().getFavoriteRestaurants(loginUser.getId()));
                request.setAttribute("error", "회원 탈퇴에 실패했습니다. 다시 시도해 주세요.");
                RequestDispatcher rd = request.getRequestDispatcher("/views/member/myPage.jsp");
                rd.forward(request, response);
            }
        } catch (Exception ex) {
            // DB 에러 메시지를 화면에 표시해 원인 파악
            request.setAttribute("reservations", ReservationDAO.getInstance().getReservationsByUserId(loginUser.getId()));
            request.setAttribute("favorites",    FavoriteDAO.getInstance().getFavoriteRestaurants(loginUser.getId()));
            request.setAttribute("error", "[DB 오류] " + ex.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/views/member/myPage.jsp");
            rd.forward(request, response);
        }
    }
}