package app.sql.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

final public class ConnectionPool {
    private static Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private DbInitializer dbInitializer;

    private ArrayBlockingQueue<PooledConnection> freeConnections;
    private ArrayBlockingQueue<PooledConnection> usedConnections;

    private static ReentrantLock lock = new ReentrantLock();

    private static ConnectionPool instance;

    public static ConnectionPool getInstance() {
        if (instance != null) {
            return instance;
        }

        try {
            lock.lock();
            if (instance == null) {
                try {
                    instance = new ConnectionPool();
                } catch (SQLException e) {
                    LOGGER.error("Can not get Instance", e);
                }
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private ConnectionPool()  {
        try {
            dbInitializer = new DbInitializer();
            freeConnections = new ArrayBlockingQueue<>(dbInitializer.getDB_INITIAL_CAPACITY());
            usedConnections = new ArrayBlockingQueue<>(dbInitializer.getDB_INITIAL_CAPACITY());
            Class.forName(dbInitializer.getDB_DRIVER());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {

        for (int i = 0; i < dbInitializer.getDB_INITIAL_CAPACITY(); i++) {
            try {
                Connection connection = DriverManager.getConnection(dbInitializer.getDB_URL(), dbInitializer.getDB_USER(), dbInitializer.getDB_PASSWORD());
                freeConnections.add(new PooledConnection(connection));
            } catch (SQLException | IllegalStateException e) {
                LOGGER.error("Pool can not initialize", e);
            }
        }

    }

    void freeConnection(PooledConnection connection) throws
            SQLException {
        lock.lock();
        try {
            if (connection.isValid(dbInitializer.getDB_CONNECTION_TIMEOUT())) {
                connection.clearWarnings();
                connection.setAutoCommit(true);
                usedConnections.remove(connection);
                freeConnections.put(connection);
                LOGGER.debug(
                        String.format("Connection was returned into pool. " +
                                        "Current pool size: %d used " +
                                        "connections;" +
                                        " %d free connection",
                                usedConnections.size(),
                                freeConnections.size()));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.warn("It is impossible to return database " +
                    "connection into pool", e);
            connection.getConnection().close();
        }finally {
            lock.unlock();
        }
    }

}
