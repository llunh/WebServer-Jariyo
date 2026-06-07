package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mvc.database.DBConnection;

public class FavoriteDAO {

    private static FavoriteDAO instance;

    private FavoriteDAO() {}

    public static FavoriteDAO getInstance() {
        if (instance == null)
            instance = new FavoriteDAO();
        return instance;
    }

    // 관심 식당 여부 확인
    public boolean isFavorite(int userId, int restaurantId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        ResultSet         rs     = null;
        boolean           result = false;

        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND restaurant_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, restaurantId);
            rs    = pstmt.executeQuery();
            if (rs.next())
                result = rs.getInt(1) > 0;
        } catch (Exception ex) {
            System.out.println("isFavorite() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {}
        }
        return result;
    }

    // 관심 식당 추가
    public boolean addFavorite(int userId, int restaurantId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "INSERT INTO favorites (user_id, restaurant_id) VALUES (?, ?)";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, restaurantId);
            result = pstmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("addFavorite() 예외발생: " + ex);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {}
        }
        return result;
    }

    // 관심 식당 취소
    public boolean removeFavorite(int userId, int restaurantId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "DELETE FROM favorites WHERE user_id = ? AND restaurant_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, restaurantId);
            result = pstmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("removeFavorite() 예외발생: " + ex);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {}
        }
        return result;
    }

    // 관심 식당 목록 조회
    public ArrayList<RestaurantDTO> getFavoriteRestaurants(int userId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<RestaurantDTO> list = new ArrayList<RestaurantDTO>();

        String sql =
            "SELECT rs.id, rs.name, rs.address, rs.category, rs.image_filename " +
            "FROM favorites f " +
            "JOIN restaurants rs ON f.restaurant_id = rs.id " +
            "WHERE f.user_id = ? " +
            "ORDER BY f.created_at DESC";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs    = pstmt.executeQuery();
            while (rs.next()) {
                RestaurantDTO r = new RestaurantDTO();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setAddress(rs.getString("address"));
                r.setCategory(rs.getString("category"));
                r.setImageFilename(rs.getString("image_filename"));
                list.add(r);
            }
        } catch (Exception ex) {
            System.out.println("getFavoriteRestaurants() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {}
        }
        return list;
    }
}