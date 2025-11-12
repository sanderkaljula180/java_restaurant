package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Waitress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WaitressRepository {

    private final DBConnectionPool cp;

    public WaitressRepository(DBConnectionPool cp) {
        this.cp = cp;
    }

    public Waitress findWaitressNameById(int waitress_id) throws SQLException {
        String sqlStatement = "SELECT id, name, is_available FROM waitresses WHERE id = ?";
        Waitress waitress = new Waitress();
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, waitress_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    waitress.setId(resultSet.getInt(1));
                    waitress.setName(resultSet.getString(2));
                    waitress.setAvailable(resultSet.getBoolean(3));
                }
            }
        }
        return waitress;
    }
}
