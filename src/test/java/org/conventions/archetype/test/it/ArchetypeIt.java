package org.conventions.archetype.test.it;

import junit.framework.Assert;
import org.conventions.archetype.model.User;
import org.conventions.archetype.test.it.role.RoleIt;
import org.conventions.archetype.test.it.user.UserIt;
import org.conventions.archetype.test.rest.UserRestTest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;

import javax.inject.Inject;
import java.net.URL;

public class ArchetypeIt extends BaseIt {

    @Inject
    RoleIt roleIT;

    @Inject
    UserIt userIt;


    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldListUsers(){
        userIt.shouldListUsers();

    }

    @Test
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldInsertUser() {
       userIt.shouldInsertUser();
    }
    
   
    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFindUser() {
        userIt.shouldFindUser();
    }


    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFailToRemoveUserWithoutPermission() {
        User user = userService.getDao().get(2L);
        //TODO decripty user pass
        super.login(user.getName(),"user");
        //looged in user has no permition to remove user
        userIt.shouldFailToRemoveUserWithoutPermission(user);
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFailToRemoveUserWithGroups() {
        super.login("arun","42");//arun has permission
        //user with id 1 has groups
        User user = userService.getDao().get(1L);
        userIt.shouldFailToRemoveUserWithGroups(user);
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldRemoveUser() {
        //user 2 has no groups
        super.login("arun","42");
        User user = userService.getDao().get(2L);
        userIt.shouldRemoveUser(user);
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
            throw new RuntimeException("Problem to insert user:" + e.getMessage()+ " - "+e.getCause() + " - " + e.getLocalizedMessage());
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
            userRestTest.shouldFindUserByName("user rest");
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
