package mvc.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mvc.model.ReservationDAO;
import mvc.model.ReservationDTO;
import mvc.model.UserDTO;

public class ReservationController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestURI  = request.getRequestURI();
        String contextPath = request.getContextPath();
        String command     = requestURI.substring(contextPath.length());

        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("utf-8");

        if (command.equals("/ReservationAction.do")) {
            requestReservation(request, response);

        } else if (command.equals("/ReservationCancelAction.do")) {
            requestCancel(request, response);
        }
    }

    // 예약 처리
    private void requestReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session   = request.getSession(false);
        UserDTO     loginUser = (UserDTO) session.getAttribute("loginUser");

        int    restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
        String date         = request.getParameter("date");
        String time         = request.getParameter("time");
        int    partySize    = Integer.parseInt(request.getParameter("partySize"));

        ReservationDTO r = new ReservationDTO();
        r.setUserId(loginUser.getId());
        r.setRestaurantId(restaurantId);
        r.setReservationDate(date);
        r.setReservationTime(time);
        r.setPartySize(partySize);

        int result = ReservationDAO.getInstance().insertReservation(r);

        // 결과를 request에 담아 뷰로 전달
        request.setAttribute("result",      result);
        request.setAttribute("date",        date);
        request.setAttribute("time",        time);
        request.setAttribute("partySize",   partySize);

        RequestDispatcher rd = request.getRequestDispatcher("/views/reservation/result.jsp");
        rd.forward(request, response);
    }

    // 예약 취소 처리 — 취소 후 마이페이지로 이동
    private void requestCancel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session       = request.getSession(false);
        UserDTO     loginUser     = (UserDTO) session.getAttribute("loginUser");
        int         reservationId = Integer.parseInt(request.getParameter("reservationId"));

        boolean ok = ReservationDAO.getInstance().cancelReservation(reservationId, loginUser.getId());

        if (ok) {
            response.sendRedirect(request.getContextPath() + "/MemberMyPage.do");
        } else {
            response.sendRedirect(request.getContextPath() + "/MemberMyPage.do?cancelError=past");
        }
    }
}