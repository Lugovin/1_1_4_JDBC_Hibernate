package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Util util = Util.getInstance(); // Создаем пул коннеторов

    public static void main(String[] args) throws SQLException {
        UserService userService = new UserServiceImpl();


//       1. Создание таблицы User(ов)
        userService.createUsersTable();
//
//      2. Добавление 4 User(ов) в таблицу с данными на свой выбор.
//      После каждого добавления должен быть вывод в консоль (User с именем — name добавлен в базу данных)
        List<User> users = new ArrayList<>();
        users.add(new User("kolya", "lugovin", (byte) 39));
        users.add(new User("elena", "petrova", (byte) 37));
        users.add(new User("oleg", "ivanov", (byte) 9));
        users.add(new User("fedot", "fedotov", (byte) 50));

        for (User user : users) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
        }


//        3. Получение всех User из базы и вывод в консоль (должен быть переопределен toString в классе User)
        System.out.println(userService.getAllUsers());


//        4. Очистка таблицы User(ов)
        userService.cleanUsersTable();


//        5. Удаление таблицы
        userService.dropUsersTable();


        try {
            util.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
