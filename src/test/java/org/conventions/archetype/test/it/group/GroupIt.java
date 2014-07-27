package org.conventions.archetype.test.it.group;

import junit.framework.Assert;
import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.model.User;
import org.conventions.archetype.service.GroupService;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rmpestano on 3/9/14.
 * injected in UserIt to avoid multiple arquillian deployments
 */
public class GroupIt implements Serializable {

    @Inject
    GroupService groupService;


    public void shoulFindAvailableRoles() {
        Group group = groupService.crud().get(1L);
        assertNotNull(group);
        //as group1 has role1, avaiable roles are the ones group1 doesnt have(role2 in this case)
        List<Role> rolesFound = groupService.findAvailableRoles(group);
        Assert.assertNotNull(rolesFound);
        assertEquals(rolesFound.size(), 1);
        for (Role role : rolesFound) {
            role.getName().equals("operator");
        }
    }


    public void shouldPaginateGroupsWithRole(String roleName, int expected) {
        SearchModel<Group> searchModel = new SearchModel<>(new Group());
        searchModel.getDatatableFilter().put("roles", roleName);
        searchModel.setFirst(0);
        searchModel.setPageSize(5);
        PaginationResult<Group> result = groupService.executePagination(searchModel);
        assertNotNull(result);
        assertEquals(result.getRowCount().intValue(), expected);
        boolean hasRoleName = false;
        for (Group group : result.getPage()) {
            for (Role role1 : group.getRoles()) {
                if (role1.getName().equals(roleName)) {
                    hasRoleName = true;
                    break;
                }
            }
        }
        if (expected > 0) {//if expected > 0 then user must have at least a role with name 'name'
            Assert.assertTrue(hasRoleName);
        }
    }

    public void shouldPaginateGroupsWithUserId() {
        SearchModel<Group> searchModel = new SearchModel<>(new Group());
        searchModel.addFilter("currentUser", 1L);
        searchModel.setFirst(0);
        searchModel.setPageSize(5);
        PaginationResult<Group> result = groupService.executePagination(searchModel);
        assertNotNull(result);
        assertEquals(result.getRowCount().intValue(), 1);
        boolean hasUserId = false;
        for (Group group : result.getPage()) {
            for (User user : group.getUsers()) {
                if (user.getId().equals(1L)) {
                    hasUserId = true;
                }
            }
        }
        Assert.assertTrue(hasUserId);

    }


}
