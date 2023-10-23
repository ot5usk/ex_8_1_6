package jm.task.core.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class Helper {
    public static void rollback(Connection connection) {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setAutoCommitTrue(Connection connection) {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
