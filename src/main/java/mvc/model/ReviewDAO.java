package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import mvc.database.DBConnection;
import mvc.model.ReviewLikeDAO;

public class ReviewDAO {

    private static ReviewDAO instance;

    private ReviewDAO() {}

    public static ReviewDAO getInstance() {
        if (instance == null)
            instance = new ReviewDAO();
        return instance;
    }

    // 전체 리뷰 개수
    public int getReviewCount() {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        int               count = 0;

        String sql = "SELECT COUNT(*) FROM reviews";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs    = pstmt.executeQuery();
            if (rs.next())
                count = rs.getInt(1);
        } catch (Exception ex) {
            System.out.println("getReviewCount() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return count;
    }

    // 리뷰 목록 조회 (페이징)
    public ArrayList<ReviewDTO> getReviewList(int page, int limit) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;

        int total_record = getReviewCount();
        int start        = (page - 1) * limit;
        int index        = start + 1;

        String sql =
            "SELECT r.id, r.user_id, r.restaurant_id, r.rating, r.content, " +
            "       DATE_FORMAT(r.created_at, '%Y/%m/%d %H:%i') AS created_at, " +
            "       u.nickname, rs.name AS restaurant_name " +
            "FROM reviews r " +
            "JOIN users       u  ON r.user_id       = u.id " +
            "JOIN restaurants rs ON r.restaurant_id = rs.id " +
            "ORDER BY r.id DESC";

        ArrayList<ReviewDTO> list = new ArrayList<ReviewDTO>();

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql,
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();

            while (rs.absolute(index)) {
                ReviewDTO review = new ReviewDTO();
                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRestaurantId(rs.getInt("restaurant_id"));
                review.setRating(rs.getInt("rating"));
                review.setContent(rs.getString("content"));
                review.setCreatedAt(rs.getString("created_at"));
                review.setUsername(rs.getString("nickname"));
                review.setRestaurantName(rs.getString("restaurant_name"));
                review.setImages(getImagesByReviewId(rs.getInt("id")));
                review.setLikeCount(ReviewLikeDAO.getInstance().getLikeCount(rs.getInt("id")));
                list.add(review);

                if (index < (start + limit) && index <= total_record)
                    index++;
                else
                    break;
            }
        } catch (Exception ex) {
            System.out.println("getReviewList() 예외발생: " + ex);
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

    // 특정 리뷰의 이미지 목록
    public ArrayList<ReviewImageDTO> getImagesByReviewId(int reviewId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<ReviewImageDTO> imgs = new ArrayList<ReviewImageDTO>();

        String sql = "SELECT * FROM review_images WHERE review_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            rs    = pstmt.executeQuery();
            while (rs.next()) {
                ReviewImageDTO img = new ReviewImageDTO();
                img.setId(rs.getInt("id"));
                img.setReviewId(reviewId);
                img.setFileName(rs.getString("file_name"));
                img.setOriName(rs.getString("ori_name"));
                imgs.add(img);
            }
        } catch (Exception ex) {
            System.out.println("getImagesByReviewId() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return imgs;
    }

    // 리뷰 저장 (이미지 포함 트랜잭션)
    public boolean insertReview(ReviewDTO review, ArrayList<ReviewImageDTO> images) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         keys  = null;
        boolean           result = false;

        String reviewSql = "INSERT INTO reviews (user_id, reservation_id, restaurant_id,rating, content) VALUES (?,?,?, ?, ?)";
        String imageSql  = "INSERT INTO review_images (review_id, file_name, ori_name) VALUES (?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(reviewSql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getReservationId());
            pstmt.setInt(3, review.getRestaurantId());
            pstmt.setInt(4, review.getRating());
            pstmt.setString(5, review.getContent());
            pstmt.executeUpdate();

            keys = pstmt.getGeneratedKeys();
            keys.next();
            int reviewId = keys.getInt(1);

            if (images != null && !images.isEmpty()) {
                pstmt = conn.prepareStatement(imageSql);
                for (ReviewImageDTO img : images) {
                    pstmt.setInt(1, reviewId);
                    pstmt.setString(2, img.getFileName());
                    pstmt.setString(3, img.getOriName());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
            result = true;

        } catch (Exception ex) {
            System.out.println("insertReview() 예외발생: " + ex);
            try {
                if (conn != null) conn.rollback();
            } catch (Exception e) {
                System.out.println("rollback 오류: " + e);
            }
        } finally {
            try {
                if (keys  != null) keys.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return result;
    }
    
 // 리뷰 삭제
    public boolean deleteReview(int reviewId, int userId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        // 본인 리뷰만 삭제 가능하도록 user_id 조건 추가
        String sql = "DELETE FROM reviews WHERE id = ? AND user_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            pstmt.setInt(2, userId);
            result = pstmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("deleteReview() 예외발생: " + ex);
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
    
    // 리뷰 중복 체크 메서드 -> 예약 건당 리뷰 하나
    public boolean hasReviewForReservation(int reservationId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception ex) {
            System.out.println("hasReviewForReservation() 예외발생: " + ex);
        }
        return false;
    }
    
 // 식당별 리뷰 목록 조회
    public ArrayList<ReviewDTO> getReviewsByRestaurantId(int restaurantId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        ArrayList<ReviewDTO> list = new ArrayList<ReviewDTO>();

        String sql =
            "SELECT r.id, r.user_id, r.rating, r.content, " +
            "       DATE_FORMAT(r.created_at, '%Y/%m/%d %H:%i') AS created_at, " +
            "       u.nickname " +
            "FROM reviews r " +
            "JOIN users u ON r.user_id = u.id " +
            "WHERE r.restaurant_id = ? " +
            "ORDER BY r.id DESC";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, restaurantId);
            rs    = pstmt.executeQuery();
            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setContent(rs.getString("content"));
                review.setCreatedAt(rs.getString("created_at"));
                review.setUsername(rs.getString("nickname"));
                review.setImages(getImagesByReviewId(rs.getInt("id")));
                review.setLikeCount(ReviewLikeDAO.getInstance().getLikeCount(rs.getInt("id")));
                list.add(review);
            }
        } catch (Exception ex) {
            System.out.println("getReviewsByRestaurantId() 예외발생: " + ex);
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