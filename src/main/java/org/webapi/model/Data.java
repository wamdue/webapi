package org.webapi.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wamdue
 * @version 1.0
 * @since 12.03.2018
 */
public class Data {
    private String  id;
    private Timestamp date;
    private Map<String, Integer> data = new HashMap<>();

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Map<String, Integer> getData() {
        return data;
    }
    public void addData(String name, int value) {
        this.data.put(name, value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
