package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// MySQL DB 연결을 담당하는 클래스
public class DBConnection {

	// DB 연결 객체 반환 — DAO에서 쿼리 실행 전에 호출
	public static Connection getConnection() throws SQLException, ClassNotFoundException {

		Connection conn = null;

		String url      = "jdbc:mysql://localhost:3306/JariyoDB"
                + "?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
                + "&allowPublicKeyRetrieval=true";
		String user = "root";
		String password = "root";

		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(url, user, password);

		return conn;
	}

}
