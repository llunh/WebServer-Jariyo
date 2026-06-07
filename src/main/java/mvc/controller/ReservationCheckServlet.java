package mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ReservationDAO;

public class ReservationCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        try {
            int    restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
            String date         = request.getParameter("date");
            String time         = request.getParameter("time");

            boolean available = ReservationDAO.getInstance().isTimeAvailable(restaurantId, date, time);
            out.print("{\"available\": " + available + "}");
        } catch (Exception e) {
            out.print("{\"available\": false}");
        }
    }
}