package org.conventions.archetype.test.it.user;

import junit.framework.Assert;
import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.User;
import org.conventions.archetype.service.UserService;
import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.util.ResourceBundle;

import javax.inject.Inject;
import java.io.Serializable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by rmpestano on 4/20/14.
 */
public class UserIt implements Serializable {

    @Inject
    UserService userService;

    @Inject
    ResourceBundle resourceBundle;

    public void shouldListUsers() {
        //dataset has 2 users
        assertEquals(2, userService.crud().countAll());
    }

    public void shouldInsertUser() {
        int userCountBefore = userService.crud().countAll();
        User user = new User();
        user.setName("name");
        user.setPassword("pass");
        userService.store(user);
        assertEquals(userCountBefore, userService.crud().countAll() - 1);
    }

    public void shouldFindUser() {
        //dataset has user with id = 1
        assertNotNull(userService.crud().get(1L));
    }

    public void shouldPaginateUserWithGroupsInFilter(String groupName,int expected){
        SearchModel<User> userSearch = new SearchModel<>();
        userSearch.addFilter("groups",groupName);
        userSearch.setFirst(0);
        userSearch.setPageSize(5);
        PaginationResult<User> result = userService.executePagination(userSearch);
        Assert.assertNotNull(result);
        assertEquals(result.getRowCount().intValue(),expected);
        boolean hasGroup = false;
        for (User user1 : result.getPage()) {
            for (Group group : user1.getGroups()) {
                if(group.getName().equals(groupName)){
                    hasGroup = true;
                }
            }
        }

        if(expected > 0){
            org.junit.Assert.assertTrue(hasGroup);
        }

    }

    public void shouldPaginateUserWithGroups(String groupName,int expected){
        User user = new User();
        user.setGroup(new Group(groupName));
        SearchModel<User> userSearch = new SearchModel<>(user);
        userSearch.setFirst(0);
        userSearch.setPageSize(5);
        PaginationResult<User> result = userService.executePagination(userSearch);
        Assert.assertNotNull(result);
        assertEquals(result.getRowCount().intValue(),expected);
        boolean hasGroup = false;
        for (User user1 : result.getPage()) {
            for (Group group : user1.getGroups()) {
                if(group.getName().equals(groupName)){
                    hasGroup = true;
                    break;
                }
            }
        }
        if(expected > 0){//if expected > 0 then user must have at least a group with name 'groupName'
            assertTrue(hasGroup);
        }

    }



    @Deprecated
    /**
     * @deprecated as ArchetypeIt also has userService and its an Stateful EJB we cannot have multiple extended persistenceContext
     * so we moved this test to ArchtypeIt and use same UserService
     */
    public void shouldFailToRemoveUserWithGroups(User user) {
        assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            assertEquals(be.getMessage(), resourceBundle.getString("be.user.remove"));
        }
        assertNotNull(user);
    }

    public void shouldRemoveUser(User user) {
        assertNotNull(user);
        userService.remove(user);
        user = userService.crud().get(2L);
        assertNull(user);
    }

    public void shouldPaginateUsers() {
        SearchModel<User> searchModel = new SearchModel<>();
        searchModel.setFirst(0);
        searchModel.setPageSize(1);
        //dataset has 2 users so we must have 2 pages
        //as we are providing an empty search it should bring all users 2 users
        PaginationResult<User> result = userService.executePagination(searchModel);
        assertNotNull(result);
        assertEquals(result.getRowCount().intValue(),2);
        assertEquals(result.getPage().size(),1);
    }
}
