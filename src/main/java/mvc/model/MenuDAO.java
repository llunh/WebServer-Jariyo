package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mvc.database.DBConnection;

public class MenuDAO {

    private static MenuDAO instance;

    private MenuDAO() {}

    public static MenuDAO getInstance() {
        if (instance == null)
            instance = new MenuDAO();
        return instance;
    }

    // 특정 식당의 메뉴 전체 조회
    public ArrayList<MenuDTO> getMenusByRestaurantId(int restaurantId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<MenuDTO> list = new ArrayList<MenuDTO>();

        String sql = "SELECT * FROM menus WHERE restaurant_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, restaurantId);
            rs    = pstmt.executeQuery();
            while (rs.next()) {
                MenuDTO menu = new MenuDTO();
                menu.setId(rs.getInt("id"));
                menu.setRestaurantId(rs.getInt("restaurant_id"));
                menu.setName(rs.getString("name"));
                menu.setPrice(rs.getInt("price"));
                menu.setDescription(rs.getString("description"));
                menu.setCategory(rs.getString("category"));
                menu.setImageFilename(rs.getString("image_filename"));
                list.add(menu);
            }
        } catch (Exception ex) {
            System.out.println("getMenusByRestaurantId() 예외발생: " + ex);
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
