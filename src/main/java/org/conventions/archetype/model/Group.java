/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.conventionsframework.model.VersionatedEntityLong;

@Entity
@SequenceGenerator(allocationSize = 1, name = "seq_group", sequenceName = "seq_group")
@Table(name="group_")
public class Group extends VersionatedEntityLong{
    
    private String name;
    private List<Role> roles;
    private List<User> users;


    public Group() {
    }
    
    public Group(String name) {
        this.name = name;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @ManyToMany(mappedBy="groups")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    

    public void addRole(Role role){
        if(getRoles() == null){
            setRoles(new ArrayList<Role>());
        }
        getRoles().add(role);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Group other = (Group) obj;
        if ((this.getId() == null) ? (other.getId() != null) : !this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
