package app.sql.pool;

import com.mysql.cj.xdevapi.SqlDataResult;
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

    private DbInitializer dbInitializer;

    private ArrayBlockingQueue<PooledConnection> freeConnections;
    private ArrayBlockingQueue<PooledConnection> usedConnections;

    private static ReentrantLock lock = new ReentrantLock();

    private volatile static ConnectionPool instance = null;

    public static ConnectionPool getInstance() {
        if (instance != null) {
            return instance;
        }
        lock.lock();
        try {
            instance = new ConnectionPool();
        } catch (SQLException e) {
            LOGGER.error("Error initiating ConnectionPool instance", e);
        } finally {
            lock.unlock();
        }
        return instance;
    }

    private ConnectionPool() throws SQLException {
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
            } catch (SQLException e) {
                LOGGER.error("Error initiating PoolConnection with initial connections", e);
            }
        }

    }

    private PooledConnection createConnection() throws SQLException {
        return new PooledConnection(DriverManager.getConnection(dbInitializer.getDB_URL(),
                dbInitializer.getDB_USER(),
                dbInitializer.getDB_PASSWORD()));
    }

    public Connection getConnection() {
        lock.lock();
        PooledConnection connection = null;
        try {
            while (connection == null) {
                if (!freeConnections.isEmpty()) {
                    connection = freeConnections.take();
                    if (!connection.isValid(dbInitializer.getDB_CONNECTION_TIMEOUT())) {
                        connection.getConnection().close();
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

    void releaseConnection(PooledConnection connection) throws SQLException {
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
            connection.getConnection().close();
        } finally {
            lock.unlock();
        }
    }

    public void destroy() throws SQLException {
        lock.lock();
        try {
            for (PooledConnection connection : freeConnections) {
                try {
                    connection.getConnection().close();
                } catch (SQLException e) {
                    LOGGER.error("Error destroying connections\n" + e);
                }
            }
            for (PooledConnection connection : usedConnections) {
                try {
                    connection.getConnection().close();
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
        } finally {
            lock.unlock();
        }
    }

}
