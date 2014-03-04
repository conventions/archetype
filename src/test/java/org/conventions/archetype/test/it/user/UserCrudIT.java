package org.conventions.archetype.test.it.user;

import org.conventions.archetype.model.User;
import org.conventions.archetype.security.AppSecurityContext;
import org.conventions.archetype.service.UserService;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.conventionsframework.exception.BusinessException;
import org.junit.Assert;

import javax.inject.Inject;
import java.io.Serializable;

public class UserCrudIT implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    UserService userService;

    @Inject
    AppSecurityContext securityContext;

    public void shouldInsertUser() {
        int userCountBefore = userService.getDao().countAll();
        User user = new User();
        user.setName("name");
        user.setPassword("pass");
        userService.store(user);
        Assert.assertEquals(userCountBefore, userService.getDao().countAll() - 1);
    }

    public void shouldListUsers() {
        //dataset has 2 users
        Assert.assertEquals(2, userService.getDao().countAll());
    }

    public void shouldFindUser() {
        //dataset has user with id = 1
        Assert.assertNotNull(userService.getDao().get(1L));
    }


    public void shouldFailToRemoveUser() {
        securityContext.setUser(userService.getDao().get(2L));
        securityContext.doLogon();
        //looged in user has no permition to remove user
        User user = userService.getDao().get(2L);
        Assert.assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            Assert.assertEquals(be.getMessage(), TestMessageProvider.getMessage("default-security-message"));
        }
    }

    public void shouldFailToRemoveUserWithGroups() {
        //user with id 1 has groups
        User user = userService.getDao().get(1L);
        Assert.assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            Assert.assertEquals(be.getMessage(), TestMessageProvider.getMessage("be.user.remove"));
        }
        Assert.assertNotNull(user);
    }

    public void shouldRemoveUser() {
        //user 2 has no groups
        User user = userService.getDao().get(2L);
        Assert.assertNotNull(user);
        userService.remove(user);
        user = userService.getDao().get(2L);
        Assert.assertNull(user);
    }


}
