package org.conventions.archetype.test.it.role;

import junit.framework.Assert;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.service.RoleService;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/9/14.
 * injected in UserIt to avoid multiple arquillian deployments
 */
public class RoleIt implements Serializable {

    @Inject
    RoleService roleService;

    public void shouldListRoles() {
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

    public void shouldPaginateRolesWithName(String name,int expected) {
        SearchModel<Role> searchModel = new SearchModel<>(new Role());
        searchModel.getEntity().setName(name);
        searchModel.setFirst(0);
        searchModel.setPageSize(5);
        PaginationResult<Role> result = roleService.executePagination(searchModel);
        assertNotNull(result);
        assertEquals(result.getRowCount().intValue(), expected);
        boolean hasRoleName = false;
        for (Role role1 : result.getPage()) {
                if(role1.getName().equals(name)){
                    hasRoleName = true;
                    break;
                }
            }
        if(expected > 0){//if expected > 0 then user must have at least a role with name 'name'
            Assert.assertTrue(hasRoleName);
        }
    }

    public void shouldPaginateRolesWithId(Long id,int expected) {
        SearchModel<Role> searchModel = new SearchModel<>(new Role());
        searchModel.getEntity().setId(id);
        searchModel.setFirst(0);
        searchModel.setPageSize(5);
        PaginationResult<Role> result = roleService.executePagination(searchModel);
        assertNotNull(result);
        assertEquals(result.getRowCount().intValue(), expected);
        boolean hasRoleId = false;
        for (Role role1 : result.getPage()) {
            if(role1.getId().equals(id)){
                hasRoleId = true;
                break;
            }
        }
        if(expected > 0){//if expected > 0 then user must have at least a role with name 'name'
            Assert.assertTrue(hasRoleId);
        }
    }

    public void shouldPaginateRolesWithIdsInSearchFilter(List<Long> ids,int expected) {
        SearchModel<Role> searchModel = new SearchModel<>(new Role());
        searchModel.addFilter("groupRoles",ids);
        searchModel.setFirst(0);
        searchModel.setPageSize(5);
        //will perform a not in 'ids'
        PaginationResult<Role> result = roleService.executePagination(searchModel);
        assertNotNull(result);
        assertEquals(result.getRowCount().intValue(), expected);
    }
}
