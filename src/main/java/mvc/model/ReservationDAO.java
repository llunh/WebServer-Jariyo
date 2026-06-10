package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mvc.database.DBConnection;

public class ReservationDAO {

    private static ReservationDAO instance;

    private ReservationDAO() {}

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

    // 같은 유저가 같은 식당·같은 날짜에 이미 예약했는지 확인
    private boolean isDuplicate(Connection conn, ReservationDTO r) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations " +
                     "WHERE user_id = ? AND restaurant_id = ? AND reservation_date = ? AND status = 'CONFIRMED'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, r.getUserId());
        pstmt.setInt(2, r.getRestaurantId());
        pstmt.setString(3, r.getReservationDate());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

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

    private int getMaxCapacity(Connection conn, int restaurantId) throws Exception {
        String sql = "SELECT max_capacity FROM restaurants WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, restaurantId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt("max_capacity");
    }

    // 예약 INSERT — 트랜잭션 처리 — 1:성공 / 0:정원 초과 / -1:DB 오류
    public int insertReservation(ReservationDTO r) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO reservations "
                   + "(user_id, restaurant_id, reservation_date, reservation_time, party_size, status) "
                   + "VALUES (?, ?, ?, ?, ?, 'CONFIRMED')";
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 같은 날짜에 이미 예약이 있으면 중복 예약 차단 (리턴 2)
            if (isDuplicate(conn, r)) {
                conn.rollback();
                return 2;
            }

            // 해당 시간대 예약 건수가 max_capacity 초과 시 차단 (리턴 0)
            int currentCount = getReservationCount(conn, r);
            int maxCapacity  = getMaxCapacity(conn, r.getRestaurantId());

            if (currentCount >= maxCapacity) {
                conn.rollback();
                return 0;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, r.getUserId());
            pstmt.setInt(2, r.getRestaurantId());
            pstmt.setString(3, r.getReservationDate());
            pstmt.setString(4, r.getReservationTime());
            pstmt.setInt(5, r.getPartySize());
            pstmt.executeUpdate();

            conn.commit();
            return 1;

        } catch (Exception ex) {
            System.out.println("insertReservation() 예외발생: " + ex);
            try { if (conn != null) conn.rollback(); } catch (Exception e) {}
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn  != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    // 예약 가능 여부 확인 (AJAX용)
    public boolean isTimeAvailable(int restaurantId, String date, String time) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            ReservationDTO r = new ReservationDTO();
            r.setRestaurantId(restaurantId);
            r.setReservationDate(date);
            r.setReservationTime(time);
            int current = getReservationCount(conn, r);
            int maxCap  = getMaxCapacity(conn, restaurantId);
            return current < maxCap;
        } catch (Exception e) {
            System.out.println("isTimeAvailable() 예외: " + e);
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    // 예약 취소
    public boolean cancelReservation(int reservationId, int userId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "UPDATE reservations SET status = 'CANCELLED' " +
                     "WHERE id = ? AND user_id = ? " +
                     "AND CONCAT(reservation_date, ' ', reservation_time) > NOW()";

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
    
    // 리뷰 작성 가능한 예약 목록(방문 완료 + 리뷰 미작성)
    public ArrayList<ReservationDTO> getReviewableReservations(int userId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<ReservationDTO> list = new ArrayList<ReservationDTO>();

        String sql =
            "SELECT res.id, res.reservation_date, res.reservation_time, " +
            "       res.restaurant_id, r.name AS restaurant_name " +
            "FROM reservations res " +
            "JOIN restaurants r ON res.restaurant_id = r.id " +
            "LEFT JOIN reviews rv ON rv.reservation_id = res.id " +
            "WHERE res.user_id = ? " +
            "  AND res.status = 'CONFIRMED' " +
            "  AND CONCAT(res.reservation_date, ' ', res.reservation_time) < NOW() " +
            "  AND rv.id IS NULL " +
            "ORDER BY res.reservation_date DESC";

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
                r.setRestaurantName(rs.getString("restaurant_name"));
                list.add(r);
            }
        } catch (Exception ex) {
            System.out.println("getReviewableReservations() 예외발생: " + ex);
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
