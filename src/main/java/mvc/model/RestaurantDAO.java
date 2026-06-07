package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mvc.database.DBConnection;

public class RestaurantDAO {

    private static RestaurantDAO instance;

    private RestaurantDAO() {}

    public static RestaurantDAO getInstance() {
        if (instance == null)
            instance = new RestaurantDAO();
        return instance;
    }

    // 유저가 방문한 식당 목록 (CONFIRMED 예약이고 예약 시간이 지난 것)
    public ArrayList<RestaurantDTO> getVisitedRestaurants(int userId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<RestaurantDTO> list = new ArrayList<RestaurantDTO>();

        String sql =
            "SELECT DISTINCT rs.id, rs.name, rs.address, rs.category " +
            "FROM restaurants rs " +
            "JOIN reservations rv ON rs.id = rv.restaurant_id " +
            "WHERE rv.user_id = ? " +
            "  AND rv.status = 'CONFIRMED' " +
            "  AND CONCAT(rv.reservation_date, ' ', rv.reservation_time) < NOW() " +
            "ORDER BY rs.name ASC";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs    = pstmt.executeQuery();
            while (rs.next()) {
                RestaurantDTO restaurant = new RestaurantDTO();
                restaurant.setId(rs.getInt("id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setCategory(rs.getString("category"));
                list.add(restaurant);
            }
        } catch (Exception ex) {
            System.out.println("getVisitedRestaurants() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return list;
    }

    // 유저가 해당 식당을 실제로 방문했는지 확인 (서버 사이드 검증용)
    public boolean hasVisited(int userId, int restaurantId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        ResultSet         rs     = null;
        boolean           result = false;

        String sql =
            "SELECT COUNT(*) FROM reservations " +
            "WHERE user_id = ? AND restaurant_id = ? " +
            "  AND status = 'CONFIRMED' " +
            "  AND CONCAT(reservation_date, ' ', reservation_time) < NOW()";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, restaurantId);
            rs    = pstmt.executeQuery();
            if (rs.next())
                result = rs.getInt(1) > 0;
        } catch (Exception ex) {
            System.out.println("hasVisited() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return result;
    }

    // 식당 목록 전체 조회
    public ArrayList<RestaurantDTO> getAllRestaurants() {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<RestaurantDTO> list = new ArrayList<RestaurantDTO>();

        String sql = "SELECT * FROM restaurants ORDER BY name ASC";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs    = pstmt.executeQuery();
            while (rs.next()) {
                RestaurantDTO restaurant = new RestaurantDTO();
                restaurant.setId(rs.getInt("id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setCategory(rs.getString("category"));
                list.add(restaurant);
            }
        } catch (Exception ex) {
            System.out.println("getAllRestaurants() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return list;
    }
}