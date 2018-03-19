package org.webapi.model;

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
}
