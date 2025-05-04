package jm.task.core.jdbc.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Util {
    // реализуйте настройку соеденения с БД
    private static final String USERNAME = "root";
    private static final String PASSWORD = "owner";
    private static final String URL = "jdbc:mysql://localhost:3306/db114";
    private Connection connect;

    public Util() {
        try {
            connect = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connect;
    }
}
