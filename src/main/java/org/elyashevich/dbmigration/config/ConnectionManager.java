package org.elyashevich.dbmigration.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    public static Connection getConnection(
            final String driver,
            final String username,
            final String url,
            final String password
    ) {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            LOGGER.info("Connected to db.");
        } catch (ClassNotFoundException | SQLException  exception) {
            exception.printStackTrace();
        }
        return  connection;
    }
}
