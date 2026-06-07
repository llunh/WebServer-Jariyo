package mvc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        Connection conn = null;

        String url = "jdbc:mysql://localhost:3306/JariyoDB"
                + "?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
                + "&allowPublicKeyRetrieval=true"; 
        String user     = "root";
        String password = "root";

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, password);

        return conn;
    }
}