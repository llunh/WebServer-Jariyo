package mvc.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mvc.model.FavoriteDAO;
import mvc.model.UserDTO;

public class FavoriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session   = request.getSession(false);
        UserDTO     loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/MemberLoginForm.do");
            return;
        }

        int    restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
        String referer      = request.getHeader("Referer");

        FavoriteDAO dao = FavoriteDAO.getInstance();

        if (dao.isFavorite(loginUser.getId(), restaurantId)) {
            dao.removeFavorite(loginUser.getId(), restaurantId);
        } else {
            dao.addFavorite(loginUser.getId(), restaurantId);
        }

        if (referer != null) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/restaurants.jsp");
        }
    }
}