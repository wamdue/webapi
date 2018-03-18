package org.webapi.dao;

import org.webapi.config.Connect;
import org.webapi.model.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Wamdue
 * @version 1.0
 * @since 12.03.2018
 */
public class EntryDao {
    private Connection connection;
    private Properties props = Connect.CONNECT.getProps();

    public EntryDao(Connection connection) {
        this.connection = connection;
    }

    public String insertHead(Data data) {
        String result = "error";
        try (PreparedStatement statement = this.connection.prepareStatement(this.props.getProperty("insert_head"))) {
            String id = UUID.nameUUIDFromBytes(data.getDate().toString().getBytes()).toString();
            statement.setString(1, id);
            statement.setTimestamp(2, data.getDate());
            statement.executeUpdate();

            data.setId(id);
            this.insertBody(data);
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
        Data data = null;
        try {
            set.next();
            data.setId(set.getString("id"));
            data.setDate(set.getTimestamp("date"));
            data.addData(set.getString("name"), set.getInt("value"));
            while (set.next()) {
                data.addData(set.getString("name"), set.getInt("value"));
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
            for (Map.Entry<String, Integer> map : data.getData().entrySet()) {
                statement.addBatch(String.format(props.getProperty("insert_body"), data.getId(), map.getKey(), map.getValue()));
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
