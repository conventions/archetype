package org.conventions.archetype.test.it;

import junit.framework.Assert;
import org.conventions.archetype.test.it.user.UserCrudIT;
import org.conventions.archetype.test.unit.UserRestTest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.net.URL;

public class IT extends BaseIT {

    @Inject
    UserCrudIT userCrudIT;


    @Before
    public void init() {
        login("arun", "42");
    }


    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldListUsers() {
        userCrudIT.shouldListUsers();
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFindUser() {
        userCrudIT.shouldFindUser();
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldInsertUser() {
        userCrudIT.shouldInsertUser();
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFailToRemoveUser() {
        userCrudIT.shouldFailToRemoveUser();
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldFailToRemoveUserWithGroups() {
        userCrudIT.shouldFailToRemoveUserWithGroups();
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldRemoveUser() {
        userCrudIT.shouldRemoveUser();
    }

    //REST
    @Test
    @RunAsClient
    public void shouldInsertUserViaRest(@ArquillianResource
                                                  URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest();
            userRestTest.setCONTEXT(contextPath.toString());
            userRestTest.shouldInsertUser();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to insert user:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    public void shouldFindUserViaRest(@ArquillianResource
                                        URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest();
            userRestTest.setCONTEXT(contextPath.toString());
            userRestTest.shouldFindUser();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to find user:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    public void shouldListUsersViaRest(@ArquillianResource
                                      URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest();
            userRestTest.setCONTEXT(contextPath.toString());
            userRestTest.shouldListUsersWithSuccess();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to list users:" + e.getMessage());
        }
    }

    @Test
    @RunAsClient
    public void shouldNotDeleteUserWithGroups(@ArquillianResource
                                       URL contextPath) {
        Assert.assertNotNull(contextPath);
        try {
            //leverage rest unit test
            UserRestTest userRestTest = new UserRestTest();
            userRestTest.setCONTEXT(contextPath.toString());
            userRestTest.shouldNotDeleteUserWithGroups();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Problem to delete user:" + e.getMessage());
        }
    }


}
