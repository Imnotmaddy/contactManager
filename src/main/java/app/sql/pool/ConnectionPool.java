package app.sql.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

final public class ConnectionPool {
    private static Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private DbInitializer dbInitializer = DbInitializer.getInstance();

    private ArrayBlockingQueue<Connection> freeConnections;
    private ArrayBlockingQueue<Connection> usedConnections;

    private static ReentrantLock lock = new ReentrantLock();

    private static class ConnectionPoolHolder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return ConnectionPoolHolder.INSTANCE;
    }

    private ConnectionPool() {
        // todo: make it singleton
        freeConnections = new ArrayBlockingQueue<>(dbInitializer.getDB_INITIAL_CAPACITY());
        usedConnections = new ArrayBlockingQueue<>(dbInitializer.getDB_MAX_CAPACITY());
        try {
            Class.forName(dbInitializer.getDB_DRIVER());
        } catch (ClassNotFoundException e) {
            //todo: change to logger
            e.printStackTrace();
        }
        init();
    }

    private void init() {

        for (int i = 0; i < dbInitializer.getDB_INITIAL_CAPACITY(); i++) {
            try {
                Connection connection = DriverManager.getConnection(
                        dbInitializer.getDB_URL(),
                        dbInitializer.getDB_USER(),
                        dbInitializer.getDB_PASSWORD());
                freeConnections.add(connection);
            } catch (SQLException e) {
                LOGGER.error("Error initiating PoolConnection with initial connections", e);
            }
        }

    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbInitializer.getDB_URL(),
                dbInitializer.getDB_USER(),
                dbInitializer.getDB_PASSWORD());
    }

    public Connection getConnection() {
        lock.lock();
        Connection connection = null;
        try {
            while (connection == null) {
                if (!freeConnections.isEmpty()) {
                    connection = freeConnections.take();
                    if (!connection.isValid(dbInitializer.getDB_CONNECTION_TIMEOUT())) {
                        connection.close();
                        connection = null;
                    }
                } else if (usedConnections.size() < dbInitializer.getDB_MAX_CAPACITY()) {
                    connection = createConnection();
                } else {
                    LOGGER.error("Used Connection size exceeded its limits");
                    throw new SQLException();
                }
                usedConnections.add(connection);
                LOGGER.debug(
                        String.format("Connection was received from pool. " +
                                        "Current pool size: %d used connections;" +
                                        " %d free connection", usedConnections.size(),
                                freeConnections.size()));
            }


        } catch (InterruptedException | SQLException e) {
            LOGGER.error("Exception while getting Connection to DB");
        } finally {
            lock.unlock();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        lock.lock();
        try {
            if (connection.isValid(dbInitializer.getDB_CONNECTION_TIMEOUT())) {
                connection.setAutoCommit(true);
                usedConnections.remove(connection);
                freeConnections.offer(connection);
                LOGGER.debug(
                        String.format("Connection was returned into pool. " +
                                        "Current pool size: %d used " +
                                        "connections;" +
                                        " %d free connection",
                                usedConnections.size(),
                                freeConnections.size()));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to return a Connection to freeConnections");
        } finally {
            lock.unlock();
        }
    }

    public void destroy() {
        for (Connection connection : freeConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Error destroying connections\n" + e);
            }
        }
        for (Connection connection : usedConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Error destroying connections\n" + e);
            }
        }
        freeConnections.clear();
        usedConnections.clear();

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                LOGGER.error("Failed to deregister driver");
            }
        }

        LOGGER.info("Every connection to DB is closed");

    }

}
