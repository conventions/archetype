package org.conventions.archetype.test.it;

import org.conventions.archetype.model.User;
import org.conventions.archetype.test.it.role.RoleIt;
import org.conventions.archetype.test.it.user.UserIt;
import org.conventionsframework.exception.BusinessException;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional(value = TransactionMode.DISABLED)
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
        assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            assertEquals(be.getMessage(), resourceBundle.getString("default-security-message"));
        }
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




}
