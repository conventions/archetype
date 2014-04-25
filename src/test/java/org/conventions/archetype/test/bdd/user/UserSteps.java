package org.conventions.archetype.test.bdd.user;

import org.conventions.archetype.model.Role;
import org.conventions.archetype.model.User;
import org.conventions.archetype.test.bdd.BaseStep;
import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.qualifier.LoggedIn;
import org.hibernate.criterion.MatchMode;
import org.jbehave.core.annotations.*;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

import static org.junit.Assert.*;

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

    @Inject
    @LoggedIn
    private Instance<User> loggedUser;

    @BeforeStory
    public void setUpStory(){
        testService.initDatabaseWithUserAndGroups();
    }

    @BeforeScenario
    public void setUpScenario(){
       /* super.login("admin", "admin");
        assertTrue(securityContext.loggedIn());
        assertEquals(loggedUser.get().getName(),"admin");
           */
    }

    @When("i search users with role $name")
    public void searchUserByRole(@Named("name") String roleName){
           totalUsersFound = userService.findUserByRole(new Role(roleName)).size();
    }

    @Then("users found is equal to $total")
    public void usersFoundEqualTo(@Named("total") Integer total){
        assertEquals(totalUsersFound,total);
    }

    @Given("i login with user $username, $password")
    public void loginWithUser(@Named("user")String username, @Named("pass")String password){
        securityContext.doLogoff();//if user already loggedin it will not logon with different credentials
        super.login(username,password);
        assertTrue(securityContext.loggedIn());
        assertEquals(securityContext.getUser().getName(),username);
    }

    @When("i try to remove user $name")
    public void tryToRemoveUse(@Named("name")String username){
        User user = new User(username);
        stepUser = userService.getDao().findOneByExample(user, MatchMode.EXACT);
        assertNotNull(stepUser);
        assertEquals(stepUser.getName(),username);
        try{
            userService.remove(stepUser);
            message = resourceBundle.getString("user.delete.message");
        }catch (BusinessException be){
            message = be.getMessage();
        }

    }

    @Then("i receive message $message")
    public void receiveMessage(@Named("message") String message){
        assertEquals(this.message,resourceBundle.getString(message));
    }

}
