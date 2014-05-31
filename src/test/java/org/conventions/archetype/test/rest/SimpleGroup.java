package org.conventions.archetype.test.rest;

import java.util.List;

/**
 * Created by rmpestano on 3/1/14.
 */
public class SimpleGroup {
    private Long id;
    private String name;
    private List<SimpleRole> roles;

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

    public List<SimpleRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SimpleRole> roles) {
        this.roles = roles;
    }
}
