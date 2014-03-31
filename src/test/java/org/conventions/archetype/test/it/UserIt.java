package org.conventions.archetype.test.it;

import junit.framework.Assert;
import org.conventions.archetype.model.User;
import org.conventions.archetype.test.it.role.RoleIt;
import org.conventions.archetype.test.unit.UserRestTest;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.conventionsframework.exception.BusinessException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;

import javax.inject.Inject;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserIt extends BaseIt {

    @Inject
    RoleIt roleIT;


    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldListUsers(){
        //dataset has 2 users
        assertEquals(2, userService.getDao().countAll());
    }

    @Test
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldInsertUser() {
        int userCountBefore = userService.getDao().countAll();
        User user = new User();
        user.setName("name");
        user.setPassword("pass");
        userService.store(user);
        assertEquals(userCountBefore, userService.getDao().countAll() - 1);
    }
    
   
    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFindUser() {
        //dataset has user with id = 1
        assertNotNull(userService.getDao().get(1L));
    }


    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFailToRemoveUserWithoutPermission() {
        User user = userService.getDao().get(2L);
        //TODO decripty user pass
        super.login(user.getName(),"user");
        //looged in user has no permition to remove user
        assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            assertEquals(be.getMessage(), TestMessageProvider.getMessage("default-security-message"));
        }
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFailToRemoveUserWithGroups() {
        //user with id 1 has groups
        super.login("arun","42");
        User user = userService.getDao().get(1L);
        assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            assertEquals(be.getMessage(), TestMessageProvider.getMessage("be.user.remove"));
        }
        assertNotNull(user);
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldRemoveUser() {
        //user 2 has no groups
        super.login("arun","42");
        User user = userService.getDao().get(2L);
        assertNotNull(user);
        userService.remove(user);
        user = userService.getDao().get(2L);
        org.junit.Assert.assertNull(user);
    }

    @Test
    @UsingDataSet(value = "datasets/role.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldListRoles(){
        roleIT.shouldListRoles();
    }

    @Test
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldInsertRoler() {
        roleIT.shouldInsertRole();
    }

    @Test
    @UsingDataSet(value = "datasets/role.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFindRole() {
        roleIT.shouldFindRole();
    }

    @Test
    @UsingDataSet(value = "datasets/role.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldremoveRole() {
        roleIT.shouldremoveRole();
    }

    //REST
    @Test
    @RunAsClient
    @InSequence(1)
    public void shouldInsertUserViaRest(@ArquillianResource
                                                  URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest(contextPath.toString());
            userRestTest.shouldInsertUserWithGroups();;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to insert user:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void shouldInsertUserWithoutGroupsViaRest(@ArquillianResource
                                        URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest(contextPath.toString());
            userRestTest.shouldInsertUserWithoutGroup();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to insert user:" + e.getMessage());
        }
    }


    @Test
    @RunAsClient
    @InSequence(3)
    public void shouldFindUserByNameViaRest(@ArquillianResource
                                        URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest(contextPath.toString());
            userRestTest.shouldFindUserByName("name");
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to find user by name:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    @InSequence(4)
    public void shouldListUsersViaRest(@ArquillianResource
                                      URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest(contextPath.toString());
            userRestTest.shouldListUsersWithSuccess();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to list users:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    @InSequence(5)
    public void shouldNotRemoveUserWithGroups(@ArquillianResource
                                       URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            UserRestTest userRestTest = new UserRestTest(contextPath.toString());
            userRestTest.shouldNotDeleteUserWithGroups();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to delete user:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    @InSequence(6)
    public void shouldNotRemoveUserWithNoPermission(@ArquillianResource URL contextPath){
        Assert.assertNotNull(contextPath);
        try {
            UserRestTest userRestTest = new UserRestTest(contextPath.toString());
            userRestTest.shouldNotDeleteUserWithNoPermission();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to delete user:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    @InSequence(7)
    public void shouldRemoveUserWithoutGroups(@ArquillianResource URL contextPath){
        Assert.assertNotNull(contextPath);
        try {
            UserRestTest userRestTest = new UserRestTest(contextPath.toString());
            userRestTest.shouldDeleteUserWithoutGroups();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to delete user:" + e.getMessage());
        }
    }


}
