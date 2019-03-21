package app.sql.pool;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Getter
class DbInitializer {

    private static Logger LOGGER = LogManager.getLogger(DbInitializer.class);

    private final String DB_URL = "jdbc:mysql://localhost:3306/contactManager?useSSL=false&autoReconnect=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";
    private final String DB_USER = "root";

    private final String DB_PASSWORD = "1234";

    private final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private final int DB_INITIAL_CAPACITY = 10;

    private final int DB_MAX_CAPACITY = 1000;

    private final int DB_CONNECTION_TIMEOUT = 3000;

    DbInitializer() {
        /*try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.properties.connectionPool");
            DB_URL = resourceBundle.getString("url");
            DB_USER = resourceBundle.getString("user");
            DB_PASSWORD = resourceBundle.getString("password");
            DB_DRIVER = resourceBundle.getString("driver");
            DB_INITIAL_CAPACITY = Integer.valueOf(resourceBundle.getString("init.capacity"));
            DB_MAX_CAPACITY = Integer.valueOf(resourceBundle.getString("maxCapacity"));
            DB_CONNECTION_TIMEOUT = Integer.valueOf(resourceBundle.getString("connectionTimeout"));
        } catch (NumberFormatException | MissingResourceException e) {
            LOGGER.log(Level.FATAL, "Properties file error", e);
            throw new RuntimeException("Properties file error", e);
        }*/
    }
}
