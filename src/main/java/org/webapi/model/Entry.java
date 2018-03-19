package org.webapi.model;

import java.util.Objects;

/**
 * Created on 19.03.18.
 * Main map of data.
 * @author Wamdue
 * @version 1.0
 */
public class Entry {

    private String name;

    private int value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;
        Entry entry = (Entry) o;
        return getValue() == entry.getValue() &&
                Objects.equals(getName(), entry.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getValue());
    }
}
