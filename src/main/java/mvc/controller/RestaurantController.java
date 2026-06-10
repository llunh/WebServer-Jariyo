package mvc.controller;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mvc.model.RestaurantDAO;
import mvc.model.MenuDAO;
import mvc.model.RestaurantDTO;
import mvc.model.MenuDTO;

public class RestaurantController extends HttpServlet {
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

        if (command.equals("/RestaurantList.do")) {
            requestRestaurantList(request, response);

        } else if (command.equals("/RestaurantDetail.do")) {
            requestRestaurantDetail(request, response);
        }
    }

    // 식당 목록 조회
    private void requestRestaurantList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<RestaurantDTO> list = RestaurantDAO.getInstance().getAllRestaurants();
        request.setAttribute("restaurantList", list);

        RequestDispatcher rd = request.getRequestDispatcher("/views/restaurant/list.jsp");
        rd.forward(request, response);
    }

    // 식당 상세 조회
    private void requestRestaurantDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        RestaurantDTO      restaurant = RestaurantDAO.getInstance().getRestaurantById(id);
        ArrayList<MenuDTO> menus      = MenuDAO.getInstance().getMenusByRestaurantId(id);

        request.setAttribute("restaurant", restaurant);
        request.setAttribute("menus",      menus);

        // restaurantDetail.jsp로 포워딩 (리뷰, 관심식당 기능 포함)
        RequestDispatcher rd = request.getRequestDispatcher("/views/restaurant/detail.jsp");
        rd.forward(request, response);
    }
}