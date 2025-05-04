package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String BASENAME = "db114";
    private static final String TABLENAME = "people";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        Util util = new Util();
        try (Statement statement = util.getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS " + BASENAME + "." + TABLENAME + " (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  `last_name` VARCHAR(45) NOT NULL,\n" +
                    "  `age` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;");
            System.out.println("Таблица юзеров создана");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        Util util = new Util();
        try (Statement statement = util.getConnection().createStatement()) {
            statement.execute("DROP TABLE IF EXISTS " + BASENAME + "." + TABLENAME + ";");
            System.out.println("Таблица юзеров удалена из БД.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Util util = new Util();
        try (Statement statement = util.getConnection().createStatement()) {
            statement.execute("INSERT INTO " + BASENAME + "." + TABLENAME + " (name, last_name, age) VALUES ('" + name + "', '" + lastName + "', " + age + ");");
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        Util util = new Util();
        try (Statement statement = util.getConnection().createStatement()) {
            statement.execute("DELETE FROM " + BASENAME + "." + TABLENAME + " WHERE id = " + id);
            System.out.println("Удален юзер с ID " + id + ".");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Util util = new Util();
        try (Statement statement = util.getConnection().createStatement()) {
            ResultSet result = statement.executeQuery("SELECT * FROM " + BASENAME + "." + TABLENAME + ";");
            while (result.next()) {
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setLastName(result.getString("last_name"));
                user.setAge(result.getByte("age"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        Util util = new Util();
        try (Statement statement = util.getConnection().createStatement()) {
            statement.execute("DELETE FROM " + BASENAME + "." + TABLENAME + ";");
            System.out.println("Таблица юзеров очищена.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
