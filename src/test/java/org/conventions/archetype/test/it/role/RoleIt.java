package org.conventions.archetype.test.it.role;

import org.conventions.archetype.model.Role;
import org.conventions.archetype.service.RoleService;

import javax.inject.Inject;
import java.io.Serializable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/9/14.
 * injected in UserIt to avoid multiple arquillian deployments
 */
public class RoleIt implements Serializable{

    @Inject
    RoleService roleService;

    public void shouldListRoles(){
        //dataset has 3 roles
        assertEquals(3, roleService.crud().countAll());
    }

    public void shouldInsertRole() {
        int roleCountBefore = roleService.crud().countAll();
        Role role = new Role();
        role.setName("name");
        roleService.store(role);
        assertEquals(roleCountBefore, roleService.crud().countAll() - 1);
    }


    public void shouldFindRole() {
        //dataset has role with id = 1
        assertNotNull(roleService.crud().get(1L));
        Role roleExample = new Role();
        //dataset has role with name role3
        roleExample.setName("role3");
        Role roleFound = roleService.crud().example(roleExample).find();
        assertNotNull(roleFound);
        assertTrue(roleFound.getName().equals("role3"));
    }


    public void shouldremoveRole() {
        //dataset has role with id = 1
        Role role = roleService.crud().get(1L);
        assertNotNull(role);
        roleService.remove(role);
        assertNull(roleService.crud().get(1L));
    }
}
