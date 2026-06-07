package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DBConnection;
import dto.MenuDTO;

// menus 테이블 DB 조회를 담당하는 DAO — 싱글톤 패턴
public class MenuDAO {

	private static MenuDAO instance;

	private MenuDAO() {}

	// 앱 전체에서 하나의 인스턴스만 사용
	public static MenuDAO getInstance() {
		if (instance == null)
			instance = new MenuDAO();
		return instance;
	}

	// 특정 식당의 메뉴 전체 조회 (식당 상세 페이지)
	public ArrayList<MenuDTO> getMenusByRestaurantId(int restaurantId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<MenuDTO> list = new ArrayList<MenuDTO>();
		String sql = "SELECT * FROM menus WHERE restaurant_id = ?";

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, restaurantId);
			rs = pstmt.executeQuery();

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
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		return list;
	}

}
