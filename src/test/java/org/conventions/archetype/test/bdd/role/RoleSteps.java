package org.conventions.archetype.test.bdd.role;

import junit.framework.Assert;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.service.RoleService;
import org.conventions.archetype.test.bdd.BaseStep;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.conventionsframework.exception.BusinessException;
import org.hibernate.criterion.MatchMode;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.inject.Inject;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: rmpestano
 * Date: 10/31/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoleSteps extends BaseStep implements Serializable {


    @Inject
    RoleService roleService;

    private Integer rolesFound;

    private String message;

    @BeforeStory
    public void setUpStory(){
        testService.initDatabaseWithUserAndGroups();
    }

    @When("i search roles with name $name")
    public void searchRoleWithName(@Named("name") String name){
        System.out.println(roleService.getDao().findAll());
        Role role = new Role().name(name);
        rolesFound = roleService.getDao().findByExample(role, MatchMode.START).size();
    }

    @Then("roles found is equal to $total")
    public void rolesFoundEqualTo(@Named("total")Integer total){
        Assert.assertEquals(total,rolesFound);
    }

    @When("i try to remove role with name $name")
    public void removeRole(@Named("name") String name){
       Role role = roleService.getDao().findOneByExample(new Role().name(name));
       assertNotNull(role);
       assertEquals(role.getName(),name);
       try{
           roleService.remove(role);
           message = TestMessageProvider.getMessage("role.delete.message");
       }catch (BusinessException be){
           message = be.getMessage();
       }
    }

    @Then("i receive message $message")
    public void receiveMessage(@Named("message") String message){
        assertEquals(this.message,TestMessageProvider.getMessage(message));
    }

    @When("i insert role with name $roleName")
    public void insertRole(String name){
        try{
            roleService.store(new Role(name));
            assertNotNull(roleService.getDao().findOneByExample(new Role(name)));
            message = TestMessageProvider.getMessage("role.create.message");
        }catch (Throwable t){
            message = t.getMessage();
        }
    }



}
