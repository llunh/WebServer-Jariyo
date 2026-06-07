package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import mvc.database.DBConnection;

public class ReviewLikeDAO {

    private static ReviewLikeDAO instance;

    private ReviewLikeDAO() {}

    public static ReviewLikeDAO getInstance() {
        if (instance == null)
            instance = new ReviewLikeDAO();
        return instance;
    }

    // 좋아요 수 조회
    public int getLikeCount(int reviewId) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        int               count = 0;

        String sql = "SELECT COUNT(*) FROM review_likes WHERE review_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            rs    = pstmt.executeQuery();
            if (rs.next())
                count = rs.getInt(1);
        } catch (Exception ex) {
            System.out.println("getLikeCount() 예외발생: " + ex);
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

    // 이미 좋아요 눌렀는지 확인
    public boolean isLiked(int reviewId, int userId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        ResultSet         rs     = null;
        boolean           liked  = false;

        String sql = "SELECT COUNT(*) FROM review_likes WHERE review_id = ? AND user_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            pstmt.setInt(2, userId);
            rs    = pstmt.executeQuery();
            if (rs.next())
                liked = rs.getInt(1) > 0;
        } catch (Exception ex) {
            System.out.println("isLiked() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return liked;
    }

    // 좋아요 추가
    public boolean addLike(int reviewId, int userId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "INSERT INTO review_likes (review_id, user_id) VALUES (?, ?)";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            pstmt.setInt(2, userId);
            result = pstmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("addLike() 예외발생: " + ex);
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

    // 좋아요 취소
    public boolean removeLike(int reviewId, int userId) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            pstmt.setInt(2, userId);
            result = pstmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("removeLike() 예외발생: " + ex);
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
}