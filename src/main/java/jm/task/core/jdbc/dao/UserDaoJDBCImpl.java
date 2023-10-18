package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(60) NOT NULL,
                lastName VARCHAR(60) NOT NULL,
                age TINYINT UNSIGNED NOT NULL
                );
                """;
        try (var connection = Util.get()) {
            connection.setAutoCommit(false);
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(createUsersTable);
                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        String dropUsersTable = """
                DROP TABLE IF EXISTS users;
                """;
        try (var connection = Util.get()) {
            connection.setAutoCommit(false);
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(dropUsersTable);
                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String saveUser = String.format("INSERT INTO users (name, lastName, age) VALUES ('%s', '%s', '%d');", name, lastName, age);
        String result = String.format("User с именем – '%s' добавлен в базу данных", name);
        try (var connection = Util.get()) {
            connection.setAutoCommit(false);
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(saveUser);
                connection.commit();
                System.out.println(result);
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("-> rollback");
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String removeUserById = String.format("DELETE FROM users WHERE id = '%d'", id);
        try (var connection = Util.get()) {
            connection.setAutoCommit(false);
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(removeUserById);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("-> rollback");
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String getAllUsers = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (var connection = Util.get();
             var statement = connection.createStatement()) {
            var resultExecute = statement.executeQuery(getAllUsers);
            while (resultExecute.next()) {
                User user = new User();
                user.setId(resultExecute.getLong("id"));
                user.setName(resultExecute.getString("name"));
                user.setLastName(resultExecute.getString("lastName"));
                user.setAge(resultExecute.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String cleanUsersTable = "TRUNCATE TABLE live.users;";
        try (var connection = Util.get()) {
            connection.setAutoCommit(false);
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(cleanUsersTable);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("-> rollback");
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}