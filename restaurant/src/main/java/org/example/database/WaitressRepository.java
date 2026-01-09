package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Waitress;
import org.example.exceptions.DatabaseUpdateException;
import org.example.exceptions.ResourceNotAvailable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WaitressRepository {

    private final DBConnectionPool cp;

    public WaitressRepository(DBConnectionPool cp) {
        this.cp = cp;
    }

    public Waitress findWaitressById(int waitress_id) {
        String sqlStatement = "SELECT id, name, is_available FROM waitresses WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, waitress_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return helperFroCreatingWaitressObj(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
        return null;
    }

    public List<Waitress> findAllAvailableWaitresses() {
        String sqlStatement = "SELECT * FROM waitresses WHERE is_available = true";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Waitress> waitresses = new ArrayList<>();
                while (resultSet.next()) {
                    waitresses.add(helperFroCreatingWaitressObj(resultSet));
                }
                return waitresses;
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public Waitress updateTableWaitresses(Waitress waitress) {
        String sqlStatement = "UPDATE waitresses SET " +
                "name = ?, " +
                "is_available = ? " +
                "WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setString(1, waitress.getName());
            statement.setBoolean(2, waitress.isAvailable());
            statement.setInt(3, waitress.getId());
            int rowCount = statement.executeUpdate();
            if (rowCount == 1) {
                return findWaitressById(waitress.getId());
            } else {
                throw new DatabaseUpdateException("Database update query didn't work: " + statement);
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public Waitress helperFroCreatingWaitressObj(ResultSet resultSet) throws SQLException {
        return new Waitress(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getBoolean(3)
        );
    }
}
