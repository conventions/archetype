package org.conventions.archetype.test.bdd.group;

import org.conventions.archetype.model.Group;
import org.conventions.archetype.service.GroupService;
import org.conventions.archetype.test.bdd.BaseStep;
import org.conventionsframework.exception.BusinessException;
import org.hibernate.criterion.MatchMode;
import org.jbehave.core.annotations.*;

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
public class GroupSteps extends BaseStep implements Serializable {


    @Inject
    GroupService groupService;

    private Group stepGroup;

    private String message;

    @BeforeStory
    public void setUpStory(){
        testService.initDatabaseWithUserAndGroups();
    }


    @Given("i search group with name $name")
    public void searchGroup(@Named("name") String name){
        Group group = new Group(name);
        stepGroup = groupService.crud().example(group).find();
        assertNotNull(stepGroup);
        assertEquals(name,stepGroup.getName());
    }

    @When("i edit group name to $name")
    public void editGroup(String name){
        assertNotNull(stepGroup);
        stepGroup = groupService.crud().get(stepGroup.getId());
        stepGroup.setName(name);
        groupService.store(stepGroup);
    }

    @Then("group name must be $name")
    public void groupNameMustBe(@Named("name") String name){
        Group group = new Group(name);
        assertEquals(groupService.crud().example(group,MatchMode.EXACT).count(), 1);
    }

    @When("i try to remove group with name $name")
    public void tryToRemoveGroupWithName(@Named("name") String name){
        assertNotNull(stepGroup);
        try{
            groupService.remove(stepGroup);
            message = resourceBundle.getString("group.delete.message");
        }catch (BusinessException be){
            message = be.getMessage();
        }
    }

    @Then("i receive message $message")
    public void receiveMessage(@Named("message") String message){
        assertEquals(this.message,resourceBundle.getString(message));
    }



}
