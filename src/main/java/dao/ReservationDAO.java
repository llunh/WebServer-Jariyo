package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;
import dto.ReservationDTO;

// reservations 테이블 DB 처리를 담당하는 DAO — 싱글톤 패턴
public class ReservationDAO {

    private static ReservationDAO instance;

    private ReservationDAO() {}

    // 앱 전체에서 하나의 인스턴스만 사용
    public static ReservationDAO getInstance() {
        if (instance == null)
            instance = new ReservationDAO();
        return instance;
    }

    // 같은 식당 + 날짜 + 시간대의 현재 예약 수 조회
    private int getReservationCount(Connection conn, ReservationDTO r) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations "
                   + "WHERE restaurant_id = ? AND reservation_date = ? AND reservation_time = ? AND status = 'CONFIRMED'";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, r.getRestaurantId());
        pstmt.setString(2, r.getReservationDate());
        pstmt.setString(3, r.getReservationTime());

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    // 해당 식당의 max_capacity 조회
    private int getMaxCapacity(Connection conn, int restaurantId) throws Exception {
        String sql = "SELECT max_capacity FROM restaurants WHERE id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, restaurantId);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt("max_capacity");
    }

    // 예약 INSERT — 1:성공 / 0:정원 초과 / -1:DB 오류
    public int insertReservation(ReservationDTO r) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO reservations (restaurant_id, reservation_date, reservation_time, party_size, status) "
                   + "VALUES (?, ?, ?, ?, 'CONFIRMED')";

        try {
            conn = DBConnection.getConnection();

            // 현재 예약 수가 max_capacity 이상이면 예약 거절
            int currentCount = getReservationCount(conn, r);
            int maxCapacity = getMaxCapacity(conn, r.getRestaurantId());
            if (currentCount >= maxCapacity) {
                return 0;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, r.getRestaurantId());
            pstmt.setString(2, r.getReservationDate());
            pstmt.setString(3, r.getReservationTime());
            pstmt.setInt(4, r.getPartySize());
            pstmt.executeUpdate();
            return 1;
        } catch (Exception ex) {
            System.out.println("insertReservation() 예외발생: " + ex);
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

}