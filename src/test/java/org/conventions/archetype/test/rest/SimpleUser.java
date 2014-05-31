package org.conventions.archetype.test.rest;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rmpestano on 3/1/14.
 */
public class SimpleUser implements Serializable {
    private Long id;
    private String name;
    private String password;
    private List<SimpleGroup> groups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SimpleGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<SimpleGroup> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
