package app.sql.pool;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

@Getter
class DbInitializer {

    private static Logger LOGGER = LogManager.getLogger(DbInitializer.class);

    private final String DB_URL;
    private final String DB_USER;

    private final String DB_PASSWORD;

    private final String DB_DRIVER;

    private final int DB_INITIAL_CAPACITY;

    private final int DB_MAX_CAPACITY;

    private final int DB_CONNECTION_TIMEOUT;

    private static class DbInitializerHolder {
        private static final DbInitializer INSTANCE = new DbInitializer();
    }

    public static DbInitializer getInstance() {
        return DbInitializerHolder.INSTANCE;
    }

    private DbInitializer() {
        try {
            Properties properties = new Properties();
            properties.load(DbInitializer.class.getClassLoader().getResourceAsStream("properties/connectionPool.properties"));
            DB_USER = properties.getProperty("user");
            DB_URL = properties.getProperty("url");
            DB_PASSWORD = properties.getProperty("password");
            DB_DRIVER = properties.getProperty("driver");
            DB_INITIAL_CAPACITY = Integer.valueOf(properties.getProperty("initCapacity"));
            DB_MAX_CAPACITY = Integer.valueOf(properties.getProperty("maxCapacity"));
            DB_CONNECTION_TIMEOUT = Integer.valueOf(properties.getProperty("connectionTimeout"));

        } catch (IOException e) {
            LOGGER.log(Level.FATAL, "Properties file error", e);
            throw new RuntimeException("Properties file error", e);
        }
    }
}
