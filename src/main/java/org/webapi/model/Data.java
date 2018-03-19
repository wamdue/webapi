package org.webapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Main model Class.
 * @author Wamdue
 * @version 1.0
 * @since 12.03.2018
 */
public class Data {
    /**
     * Generated id.
     */
    @JsonIgnore
    private String  id;
    /**
     * Time to search.
     */
    private Timestamp date;
    /**
     * List of data.
     */
    private List<Entry> data = new ArrayList<>();

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
     * Convert list of entry to array, for correct view in json.
     * @return array.
     */
    public Entry[] getData() {
        return data.toArray(new Entry[data.size()]);
    }

    /**
     * Add new entry to list.
     * @param entry - entry.
     */
    public void addData(Entry entry) {
        this.data.add(entry);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Data data1 = (Data) o;
        return Objects.equals(id, data1.id) &&
                Objects.equals(date, data1.date) &&
                Objects.equals(data, data1.data);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, date, data);
    }
}
