package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users_table (\n" +
                " `id` INT NOT NULL AUTO_INCREMENT,\n" +
                " `name` VARCHAR(45) NOT NULL,\n" +
                " `last_name` VARCHAR(45) NOT NULL,\n" +
                " `age` INT NOT NULL,\n" +
                "  PRIMARY KEY (`id`))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8;";
        Session session = Util.getSession().openSession();
        Transaction tx1 = session.beginTransaction();
        session.createNativeQuery(sql).executeUpdate();
        tx1.commit();
        session.close();

    }

    @Override
    public void dropUsersTable() {

        Session session = Util.getSession().openSession();
        Transaction tx1 = session.beginTransaction();
        session.createNativeQuery("DROP TABLE IF EXISTS users_table;").executeUpdate();
        tx1.commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = Util.getSession().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getSession().openSession();
        Transaction tx1 = session.beginTransaction();
        User user = session.find(User.class, id);
        session.remove(user);
        session.flush();
        tx1.commit();
        session.clear();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = Util.getSession().openSession().
                createQuery("SELECT a FROM User a", User.class).getResultList();
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSession().openSession();
        Transaction tx1 = session.beginTransaction();
        session.createQuery("DELETE User").executeUpdate();
        tx1.commit();
        session.close();

    }
}
