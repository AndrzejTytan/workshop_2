package pl.danielw;

import pl.danielw.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private DbUtil() {}
    private static final String DB_URL = "jdbc:mysql://localhost:3306" +
            "/workshop2" +
            "?useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "Daniel";
    private static final String DB_PASS = "JavaDev1";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
