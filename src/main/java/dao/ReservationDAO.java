package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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

    // 특정 유저의 예약 목록 조회 (식당 이름 포함)
    public ArrayList<ReservationDTO> getReservationsByUserId(int userId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<ReservationDTO> list = new ArrayList<ReservationDTO>();

        String sql = "SELECT rv.*, rs.name AS restaurant_name " +
                     "FROM reservations rv " +
                     "JOIN restaurants rs ON rv.restaurant_id = rs.id " +
                     "WHERE rv.user_id = ? ORDER BY rv.reservation_date DESC, rv.reservation_time DESC";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs    = pstmt.executeQuery();

            while (rs.next()) {
                ReservationDTO r = new ReservationDTO();
                r.setId(rs.getInt("id"));
                r.setRestaurantId(rs.getInt("restaurant_id"));
                r.setReservationDate(rs.getString("reservation_date"));
                r.setReservationTime(rs.getString("reservation_time"));
                r.setPartySize(rs.getInt("party_size"));
                r.setStatus(rs.getString("status"));
                r.setRestaurantName(rs.getString("restaurant_name"));
                list.add(r);
            }
        } catch (Exception ex) {
            System.out.println("getReservationsByUserId() 예외발생: " + ex);
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

    // 예약 취소 — 본인 예약만 취소 가능하도록 user_id 조건 추가
    public boolean cancelReservation(int reservationId, int userId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE id = ? AND user_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);
            pstmt.setInt(2, userId);
            result = pstmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("cancelReservation() 예외발생: " + ex);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return result;
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

        String sql = "INSERT INTO reservations (user_id, restaurant_id, reservation_date, reservation_time, party_size, status) "
                   + "VALUES (?, ?, ?, ?, ?, 'CONFIRMED')";

        try {
            conn = DBConnection.getConnection();

            // 현재 예약 수가 max_capacity 이상이면 예약 거절
            int currentCount = getReservationCount(conn, r);
            int maxCapacity = getMaxCapacity(conn, r.getRestaurantId());
            if (currentCount >= maxCapacity) {
                return 0;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, r.getUserId());
            pstmt.setInt(2, r.getRestaurantId());
            pstmt.setString(3, r.getReservationDate());
            pstmt.setString(4, r.getReservationTime());
            pstmt.setInt(5, r.getPartySize());
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