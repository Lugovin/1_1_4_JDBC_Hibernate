package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Util implements AutoCloseable {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "owner";
    private static final String URL = "jdbc:mysql://localhost:3306/db114";
    private static volatile Util instance;
    private final BlockingQueue<ProxyConnection> freeConnections;
    private final Queue<ProxyConnection> givenAwayConnection;
    static final int POOL_SIZE = 6;
    private static SessionFactory session;

    private Util() {
        freeConnections = new LinkedBlockingQueue<>();
        givenAwayConnection = new LinkedList<>();
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                freeConnections.add(proxyConnection);
                System.out.println(proxyConnection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {

        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnection.add(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Взяли коннектор из пула");
        return connection;
    }


    public void closeConnection(Connection connection) {

        try {
            if (connection instanceof ProxyConnection) {
                givenAwayConnection.remove(connection);
                freeConnections.add((ProxyConnection) connection);
                System.out.println("Убрали коннектор в пул");
            } else {
                System.out.println("Не получилось сбросить соединение в лист свободных");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Util getInstance() {   //Замутил ленивый Синглтон
        Util localInstance = instance;
        if (localInstance == null) {
            synchronized (Util.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Util();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void close() throws Exception {
        for (ProxyConnection connection : givenAwayConnection) {
            if (connection != null) {
                connection.close();
            }
        }
        for (ProxyConnection connection : freeConnections) {
            if (connection != null) {
                connection.close();
            }
        }
        System.out.println("Все коннекторы закрыты");
    }


    public static SessionFactory getSession() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/db114");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "owner");
        properties.put(Environment.HBM2DDL_AUTO, "create");

        if (session == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                session = configuration.setProperties(properties).buildSessionFactory();

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return session;
    }
}