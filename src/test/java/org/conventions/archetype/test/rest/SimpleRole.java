package org.conventions.archetype.test.rest;

import java.io.Serializable;

/**
 * Created by rmpestano on 3/1/14.
 */
public class SimpleRole implements Serializable {

    Long id;
    String name;

    public SimpleRole() {
    }

    public SimpleRole(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
