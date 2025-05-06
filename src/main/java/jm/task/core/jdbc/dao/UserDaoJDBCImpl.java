package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.Main.util;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        Connection connect = util.getConnection();   // Получаем 1 коннект из пула
        try {
            String sql = "CREATE TABLE IF NOT EXISTS people (\n" +
                    " `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    " `name` VARCHAR(45) NOT NULL,\n" +
                    " `last_name` VARCHAR(45) NOT NULL,\n" +
                    " `age` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        util.closeConnection(connect);  // Возвращаем коннект в пул
    }

    public void dropUsersTable() {
        Connection connect = util.getConnection();   // Получаем 1 коннект из пула
        try {
            String sql = "DROP TABLE IF EXISTS people";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        util.closeConnection(connect);  // Возвращаем коннект в пул
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connect = util.getConnection();   // Получаем 1 коннект из пула
        try {
            String sql = "INSERT INTO people (name, last_name, age) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.execute();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        util.closeConnection(connect);  // Возвращаем коннект в пул
    }

    public void removeUserById(long id) {
        Connection connect = util.getConnection();   // Получаем 1 коннект из пула
        try {
            String sql = "DELETE FROM people WHERE id = ?";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        util.closeConnection(connect);  // Возвращаем коннект в пул
    }

    public List<User> getAllUsers() {
        Connection connect = util.getConnection();   // Получаем 1 коннект из пула
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM people";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setLastName(result.getString("last_name"));
                user.setAge(result.getByte("age"));
                users.add(user);
            }
            preparedStatement.close();
            util.closeConnection(connect);  // Возвращаем коннект в пул
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        Connection connect = util.getConnection(); // Получаем 1 коннект из пула
        try {
            String sql = "DELETE FROM people";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        util.closeConnection(connect);  // Возвращаем коннект в пул
    }
}
