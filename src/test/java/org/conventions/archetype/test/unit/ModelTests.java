package org.conventions.archetype.test.unit;

import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rmpestano on 7/28/14.
 */
@RunWith(JUnit4.class)
public class ModelTests {


    @Test
    public void shouldGetUserGroupsSize() {
        User user = new User();
        assertEquals(user.getNumGroups(), 0);
        List<Group> userGroups = new ArrayList<Group>() {{
            add(new Group("g1"));
            add(new Group("g2"));
        }};
        user.setGroups(userGroups);
        assertEquals(user.getNumGroups(), 2);
    }

    @Test
    public void shouldGetRoleAsList() {
        Group group = new Group();
        assertNull(group.getRolesAsList());
        group.setRoles(new HashSet<Role>());
        assertNotNull(group.getRolesAsList());
        assertTrue(group.getRolesAsList() instanceof ArrayList);
    }

    @Test
    public void roleAddGroupTest() {
        Role role = new Role("");
        role.addGroup(new Group());
        assertNotNull(role.getGroups());
        role.setGroups(null);
        assertNull(role.getGroups());
    }

    /**
     * just for coverage purposes
     */
    @Test
    public void userEqualsTest() {
        User user = new User("user");
        user.setId(1L);
        assertFalse(user.equals(new User()));
        assertFalse(user.equals(new Group()));
        assertFalse(user.equals(null));

        User user2 = new User("");
        user2.setId(2L);

        assertFalse(user.equals(user2));

        user2.setId(1L);
        assertTrue(user.equals(user2));
        user.setId(null);
        assertFalse(user.equals(user2));
    }

    @Test
    public void groupEqualsTest() {
        Group group = new Group("user");
        group.setId(1L);
        assertFalse(group.equals(new User()));
        assertFalse(group.equals(new Group()));
        assertFalse(group.equals(null));

        Group group2 = new Group("");
        group2.setId(2L);

        assertFalse(group.equals(group2));

        group2.setId(1L);
        assertTrue(group.equals(group2));
        group.setId(null);
        assertFalse(group.equals(group2));
    }

    @Test
    public void roleEqualsTest() {
        Role role = new Role("role");
        role.setId(1L);
        assertFalse(role.equals(new Role()));
        assertFalse(role.equals(new Group()));
        assertFalse(role.equals(null));

        Role role2 = new Role("");
        role2.setId(2L);

        assertFalse(role.equals(role2));

        role2.setId(1L);
        assertTrue(role.equals(role2));
        role.setId(null);
        assertFalse(role.equals(role2));
    }


}
