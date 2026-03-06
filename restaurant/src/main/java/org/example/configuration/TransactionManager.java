package org.example.configuration;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    private final DBConnectionPool pool;

    public TransactionManager(DBConnectionPool pool) {
        this.pool = pool;
    }

    @FunctionalInterface
    public interface SqlWork {
        void run(Connection c) throws Exception;
    }

    public void inTx(SqlWork work) {
        Connection c = null;

        try {
            c = pool.createConnection();
            c.setAutoCommit(false);

            work.run(c);

            c.commit();
        } catch (Exception e) {
            try {
                if (c != null) c.rollback();
            } catch (SQLException ignored) {}
            throw new RuntimeException(e);
        } finally {
            try {
                if (c != null) {
                    c.setAutoCommit(true);
                    c.close();
                }
            } catch (SQLException ignored) {}
        }
    }
}
