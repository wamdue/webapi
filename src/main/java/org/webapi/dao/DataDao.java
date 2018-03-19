package org.webapi.dao;

import org.webapi.config.Connect;
import org.webapi.model.Data;
import org.webapi.model.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Wamdue
 * @version 1.0
 * @since 12.03.2018
 */
public class DataDao {
    private Connection connection;
    private Properties props = Connect.CONNECT.getProps();

    public DataDao(Connection connection) {
        this.connection = connection;
    }

    public String insertHead(Data data) {
        try (PreparedStatement statement = this.connection.prepareStatement(this.props.getProperty("insert_head"))) {
            this.connection.setAutoCommit(false);
            String id = UUID.nameUUIDFromBytes(data.getDate().toString().getBytes()).toString();
            statement.setString(1, id);
            statement.setTimestamp(2, data.getDate());
            statement.executeUpdate();

            data.setId(id);
            this.insertBody(data);
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data.getId();
    }

    public Data getEntry(String value) {
        Data data = null;
        try (PreparedStatement statement = this.connection.prepareStatement(this.props.getProperty("get_data"))) {
            statement.setString(1, value);
            data = this.parseSet(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Data parseSet(ResultSet set) {
        Data data = new Data();
        Entry entry = null;
        try {
            while (set.next()) {
            entry = new Entry();
            data.setId(set.getString("id"));
            data.setDate(set.getTimestamp("head_key"));
            entry.setName(set.getString("name"));
            entry.setValue(set.getInt("value"));
            data.addData(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private void insertBody(Data data) {
        try (Statement statement = this.connection.createStatement()) {
            for (Entry entry : data.getData()) {
                statement.addBatch(String.format(props.getProperty("insert_body"), data.getId(), entry.getName(), entry.getValue()));
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
