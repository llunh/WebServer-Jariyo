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