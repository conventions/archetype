/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.model;

import org.conventionsframework.model.VersionatedEntityLong;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(allocationSize = 1, name = "seq_user", sequenceName = "seq_user")
@Table(name = "user_")
public class User extends VersionatedEntityLong {

	@NotNull
	private String name;

	@NotNull
	private String password;
	private List<Group> groups;

	public User() {
	}

	public User(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group) {
		if (getGroups() == null) {
			setGroups(new ArrayList<Group>());
		}
		getGroups().add(group);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if ((this.getId() == null) ? (other.getId() != null) : !this.getId()
				.equals(other.getId())) {
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

	@Transient
	public int getNumGroups() {
		return groups != null ? groups.size() : 0;
	}

    @Transient
    public List<String> getUserRoles() {
        List<String> userRoles = new ArrayList<>();
        if(groups != null){
            for (Group group : groups) {
                for (Role role : group.getRoles()) {
                    userRoles.add(role.getName());
                }
            }
        }
        return userRoles;
    }
}
