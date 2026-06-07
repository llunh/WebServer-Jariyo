package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DBConnection;
import dto.RestaurantDTO;

// restaurants 테이블 DB 조회를 담당하는 DAO — 싱글톤 패턴
public class RestaurantDAO {

	private static RestaurantDAO instance;

	private RestaurantDAO() {}

	// 앱 전체에서 하나의 인스턴스만 사용
	public static RestaurantDAO getInstance() {
		if (instance == null)
			instance = new RestaurantDAO();
		return instance;
	}

	// 식당 목록 전체 조회 (메인 페이지)
	public ArrayList<RestaurantDTO> getAllRestaurants() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<RestaurantDTO> list = new ArrayList<RestaurantDTO>();
		String sql = "SELECT * FROM restaurants WHERE is_active = true";

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				RestaurantDTO restaurant = new RestaurantDTO();
				restaurant.setId(rs.getInt("id"));
				restaurant.setName(rs.getString("name"));
				restaurant.setAddress(rs.getString("address"));
				restaurant.setCategory(rs.getString("category"));
				restaurant.setPhone(rs.getString("phone"));
				restaurant.setOpeningHours(rs.getString("opening_hours"));
				restaurant.setActive(rs.getBoolean("is_active"));
				restaurant.setMaxCapacity(rs.getInt("max_capacity"));
				restaurant.setImageFilename(rs.getString("image_filename"));
				list.add(restaurant);
			}
		} catch (Exception ex) {
			System.out.println("getAllRestaurants() 예외발생: " + ex);
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

	// 식당 상세 조회 (상세 페이지)
	public RestaurantDTO getRestaurantById(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		RestaurantDTO restaurant = null;
		String sql = "SELECT * FROM restaurants WHERE id = ?";

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				restaurant = new RestaurantDTO();
				restaurant.setId(rs.getInt("id"));
				restaurant.setName(rs.getString("name"));
				restaurant.setAddress(rs.getString("address"));
				restaurant.setCategory(rs.getString("category"));
				restaurant.setPhone(rs.getString("phone"));
				restaurant.setOpeningHours(rs.getString("opening_hours"));
				restaurant.setActive(rs.getBoolean("is_active"));
				restaurant.setMaxCapacity(rs.getInt("max_capacity"));
				restaurant.setImageFilename(rs.getString("image_filename"));
			}
		} catch (Exception ex) {
			System.out.println("getRestaurantById() 예외발생: " + ex);
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		return restaurant;
	}

}
