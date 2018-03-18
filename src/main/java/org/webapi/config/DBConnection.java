package org.webapi.config;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author Wamdue
 * @version 1.0
 * @since 18.03.2018
 */
class DBConnection {
    private Properties props = new Properties();
    private static final Logger LOGGER = Logger.getLogger(Connect.class);
    private Connection connection;

    DBConnection() {
        this.init();
        this.connect();
        this.createTables();
    }

    private void init() {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream input = cl.getResourceAsStream("tables.properties");
            props.load(input);
            LOGGER.info("DB properties loaded successfully!");
        } catch (IOException e) {
            LOGGER.error("DB properties cannot be loaded", e.fillInStackTrace());
        }
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            String login = this.props.getProperty("login");
            String password = this.props.getProperty("password");
            String url = this.props.getProperty("url");
            Properties p = new Properties();
            p.setProperty("user", login);
            p.setProperty("password", password);
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, p);
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
            ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);

            poolableConnectionFactory.setPool(connectionPool);
            PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Driver class not found");
        }

        LOGGER.info("Connection to db users established successfully");

    }

    private void createTables() {
        try (Statement statement = this.connection.createStatement()) {
            statement.addBatch(this.props.getProperty("head_table"));
            statement.addBatch(this.props.getProperty("body_table"));
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Connection getConnection() {
        return this.connection;
    }

    Properties getProps() {
        return this.props;
    }

}
