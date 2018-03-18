package org.webapi.config;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author Wamdue
 * @version 1.0
 * @since 12.03.2018
 */
public enum Connect {

    CONNECT;
    private DBConnection connection;

    Connect() {
        this.connection = new DBConnection();
    }

    public Connection getConnection() {
        return this.connection.getConnection();
    }

    public Properties getProps() {
        return this.connection.getProps();
    }


}
