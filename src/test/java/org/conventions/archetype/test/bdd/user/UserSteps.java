package org.conventions.archetype.test.bdd.user;

import org.conventions.archetype.model.Role;
import org.conventions.archetype.model.User;
import org.conventions.archetype.test.bdd.BaseStep;
import org.jbehave.core.annotations.*;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: rmpestano
 * Date: 10/31/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserSteps extends BaseStep implements Serializable {

    private Integer totalUsersFound;

    private User stepUser;

    private String message;

    @BeforeStory
    public void setUpStory(){
        testService.initDatabaseWithUserAndGroups();
    }

    @BeforeScenario
    public void setUpScenario(){
        super.login("admin", "admin");
        assertTrue(securityContext.loggedIn());
    }

    @When("i search users with role $name")
    public void searchUserByRole(@Named("name") String roleName){
           totalUsersFound = userService.findUserByRole(new Role(roleName)).size();
    }

    @Then("users found is equal to $total")
    public void usersFoundEqualTo(@Named("total") Integer total){
        assertEquals(totalUsersFound,total);
    }

}
