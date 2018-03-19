package org.webapi.dao;

import org.junit.Before;
import org.junit.Test;
import org.webapi.model.Data;
import org.webapi.model.Entry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created on 19.03.18.
 *
 * @author Wamdue
 * @version 1.0
 */
public class DataDaoTest {

    private Properties props;
    /**
     * Dao object.
     */
    private DataDao dao;


    /**
     * When trying to save data to db, then returning it by id, expected that objects are equal.
     */
    @Test
    public void whenInsertNewDataToDbThenMustFindItInDb() {
        Data data = new Data();
        long date = new Date().getTime();
        data.setDate(new Timestamp(date));
        Entry entry = new Entry();
        entry.setName("test 1");
        entry.setValue(500);
        data.addData(entry);
        entry = new Entry();
        entry.setName("test 2");
        entry.setValue(1000);
        data.addData(entry);

        String id = this.dao.insertHead(data);
        Data newData = this.dao.getEntry(id);
        assertThat(data.equals(newData), is(true));
    }


    /**
     * Create virtual db for test.
     * @throws ClassNotFoundException - exception, if driver not found.
     */
    @Before
    public void init() throws ClassNotFoundException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        try {
            this.dao = new DataDao(getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.createTables();
        }

    /**
     * Create files in virtual db.
     */
    private void createTables() {
        try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {
            statement.addBatch("create table if not exists head(id varchar(50) primary key, head_key timestamp with time zone);");
            statement.addBatch("create table if not exists body(head_id varchar(50) references head(id), name varchar(50), value varchar(50));");
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get connection to db.
     * @return connection.
     * @throws SQLException - exception.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:storage", "vinod", "vinod");
    }

}