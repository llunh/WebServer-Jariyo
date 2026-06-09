package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;

import mvc.database.DBConnection;

public class UserDAO {

    private static UserDAO instance;

    private UserDAO() {}

    public static UserDAO getInstance() {
        if (instance == null)
            instance = new UserDAO();
        return instance;
    }

    // 비밀번호 SHA-256 해싱
    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("해싱 오류: " + e.getMessage());
        }
    }

    // 아이디 중복 확인
    public boolean existsByUsername(String username) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        ResultSet         rs     = null;
        boolean           exists = false;

        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs    = pstmt.executeQuery();
            if (rs.next())
                exists = rs.getInt(1) > 0;
        } catch (Exception ex) {
            System.out.println("existsByUsername() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return exists;
    }

    // 이메일 중복 확인
    public boolean existsByEmail(String email) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        ResultSet         rs     = null;
        boolean           exists = false;

        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs    = pstmt.executeQuery();
            if (rs.next())
                exists = rs.getInt(1) > 0;
        } catch (Exception ex) {
            System.out.println("existsByEmail() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return exists;
    }

    // 닉네임 중복 확인
    public boolean existsByNickname(String nickname) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        ResultSet         rs     = null;
        boolean           exists = false;

        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickname);
            rs    = pstmt.executeQuery();
            if (rs.next())
                exists = rs.getInt(1) > 0;
        } catch (Exception ex) {
            System.out.println("existsByNickname() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return exists;
    }

    // 회원가입
    public boolean insertUser(UserDTO user) {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "INSERT INTO users (username, password, email, nickname) VALUES (?, ?, ?, ?)";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, sha256(user.getPassword()));
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getNickname());
            result = pstmt.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("insertUser() 예외발생: " + ex);
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

    // 로그인: username으로 유저 조회
    public UserDTO getUserByUsername(String username) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        UserDTO           user  = null;

        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs    = pstmt.executeQuery();
            if (rs.next()) {
                user = new UserDTO();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setNickname(rs.getString("nickname"));
                user.setCreatedAt(rs.getString("created_at"));
                user.setUpdatedAt(rs.getString("updated_at"));
            }
        } catch (Exception ex) {
            System.out.println("getUserByUsername() 예외발생: " + ex);
        } finally {
            try {
                if (rs    != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn  != null) conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return user;
    }

    // 비밀번호 검증
    public boolean checkPassword(String rawPassword, String storedHash) {
        return sha256(rawPassword).equals(storedHash);
    }

    // 회원 탈퇴
    public boolean deleteUser(int userId) throws Exception {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        boolean           result = false;

        String sql = "DELETE FROM users WHERE id = ?";

        try {
            conn  = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            result = pstmt.executeUpdate() == 1;
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