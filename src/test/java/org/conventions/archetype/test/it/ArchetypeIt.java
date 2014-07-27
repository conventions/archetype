package org.conventions.archetype.test.it;

import org.conventions.archetype.model.User;
import org.conventions.archetype.test.it.role.RoleIt;
import org.conventions.archetype.test.it.user.UserIt;
import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.qualifier.Config;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Transactional(value = TransactionMode.DISABLED)
public class ArchetypeIt extends BaseIt {

    @Inject
    RoleIt roleIT;

    @Inject
    UserIt userIt;

    @Inject
    @Config
    String appVersion;

    @Inject
    @Config("key.with.dots")
    String valueFromConfigProvider;


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
        User user = userService.crud().get(2L);
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
        User user = userService.crud().get(1L);
        //userIt.shouldFailToRemoveUserWithGroups(user);
        //avoid multiple persistenceContexts: javax.ejb.EJBTransactionRolledbackException: JBAS011437:
        // Found extended persistence context in SFSB invocation call stack but that cannot be used because the transaction already has a transactional context associated with it.  This can be avoided by changing application code, either eliminate the extended persistence context or the transactional context.  See JPA spec 2.0 section 7.6.3.1.  Scoped persistence unit name=2cae0ee9-0bb5-411c-bea7-e0029dc826de.war#archetypeTestPU, persistence context already in transaction =ExtendedEntityManager [2cae0ee9-0bb5-411c-bea7-e0029dc826de.war#archetypeTestPU], extended persistence context =ExtendedEntityManager [2cae0ee9-0bb5-411c-bea7-e0029dc826de.war#archetypeTestPU].
        assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            assertEquals(be.getMessage(), resourceBundle.getString("be.user.remove"));
        }
        assertNotNull(user);
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldRemoveUser() {
        //user 2 has no groups
        super.login("arun","42");
        User user = userService.crud().get(2L);
        //userIt.shouldRemoveUser(user);
        assertNotNull(user);
        userService.remove(user);
        user = userService.crud().get(2L);
        assertNull(user);
    }

    @Test
    @UsingDataSet(value = "datasets/role.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldListRoles(){
        roleIT.shouldListRoles();
    }

    @Test
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldInsertRole() {
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

    @Test
    @UsingDataSet(value = "datasets/role.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldPaginateRoleWithName() {
        //dataset has 3 roles
        roleIT.shouldPaginateRolesWithName("role2",1);
        roleIT.shouldPaginateRolesWithName("role",3);
        roleIT.shouldPaginateRolesWithName("adhiad",0);
    }

    @Test
    @UsingDataSet(value = "datasets/role.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldPaginateRoleWithIdsInSearchFilter() {
        //dataset has 3 roles with ids 1,2,3
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        roleIT.shouldPaginateRolesWithIdsInSearchFilter(ids,1);//ids not in
        ids.clear();
        ids.add(5L);
        roleIT.shouldPaginateRolesWithIdsInSearchFilter(ids,3);

    }



    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldHasPermition(){
        super.login("arun","42");
        userService.testPermission();
        //TODO user warp to check facesMessages
        assertTrue(true);//if it gets here so permission check is ok
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldPaginateUsers(){
      userIt.shouldPaginateUsers();
    }

    @Test
    @UsingDataSet(value = "datasets/user.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldPaginateUsersWithGroupName(){
        userIt.shouldPaginateUserWithGroups("group1", 1);
        userIt.shouldPaginateUserWithGroups("group2",1);
        userIt.shouldPaginateUserWithGroups("group3",0);
        userIt.shouldPaginateUserWithGroupsInFilter("group1", 1);
        userIt.shouldPaginateUserWithGroupsInFilter("group2", 1);
        userIt.shouldPaginateUserWithGroupsInFilter("group3",0);
    }


    @Test
    public void shouldNotHasPermition(){
        try {
            userService.testPermission();
        }catch (BusinessException be){
            assertEquals(be.getMessage(),"Only operator can perform this task");
        }
    }

    @Test
    public void shouldGetInjectedVersionFromConfigProvider(){
       assertEquals(appVersion,resourceBundle.getString("version"));
    }


    @Test
    public void shouldGetInjectedConfigWithDots(){
        assertEquals(valueFromConfigProvider,"keyWithDots");
    }


}
