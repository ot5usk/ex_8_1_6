package jm.task.core.jdbc.dao;

import static jm.task.core.jdbc.Helper.*;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
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
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(createUsersTable);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        String dropUsersTable = """
                DROP TABLE IF EXISTS users;
                """;
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(dropUsersTable);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String saveUser = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?);";
        String result = String.format("User с именем – '%s' добавлен в базу данных", name);
        Connection tempConnection = null;
        try (Connection connection = Util.get();
             PreparedStatement preparedStatement = connection.prepareStatement(saveUser)) {
            tempConnection = connection;
            tempConnection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            tempConnection.commit();
            tempConnection.setAutoCommit(true);
            System.out.println(result);
        } catch (SQLException e) {
            rollback(tempConnection);
            System.out.println("-> rollback");
            setAutoCommitTrue(tempConnection);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String removeUserById = "DELETE FROM users WHERE id = ?";
        Connection tempConnection = null;
        try (Connection connection = Util.get();
             PreparedStatement preparedStatement = connection.prepareStatement(removeUserById)) {
            tempConnection = connection;
            tempConnection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            tempConnection.commit();
            tempConnection.setAutoCommit(true);
        } catch (SQLException e) {
            rollback(tempConnection);
            System.out.println("-> rollback");
            setAutoCommitTrue(tempConnection);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String getAllUsers = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            ResultSet resultExecute = statement.executeQuery(getAllUsers);
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
        Connection tempConnection = null;
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            tempConnection = connection;
            tempConnection.setAutoCommit(false);
            statement.executeUpdate(cleanUsersTable);
            tempConnection.commit();
            tempConnection.setAutoCommit(true);
        } catch (SQLException e) {
            rollback(tempConnection);
            System.out.println("-> rollback");
            setAutoCommitTrue(tempConnection);
            throw new RuntimeException(e);
        }
    }

}