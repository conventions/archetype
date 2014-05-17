package org.conventions.archetype.test.it.user;

import org.conventions.archetype.model.User;
import org.conventions.archetype.service.UserService;
import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.util.ResourceBundle;

import javax.inject.Inject;
import java.io.Serializable;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by rmpestano on 4/20/14.
 */
public class UserIt implements Serializable {

    @Inject
    UserService userService;

    @Inject
    ResourceBundle resourceBundle;

    public void shouldListUsers() {
        //dataset has 2 users
        assertEquals(2, userService.getDao().countAll());
    }

    public void shouldInsertUser() {
        int userCountBefore = userService.getDao().countAll();
        User user = new User();
        user.setName("name");
        user.setPassword("pass");
        userService.store(user);
        assertEquals(userCountBefore, userService.getDao().countAll() - 1);
    }

    public void shouldFindUser() {
        //dataset has user with id = 1
        assertNotNull(userService.getDao().get(1L));
    }


    public void shouldFailToRemoveUserWithGroups(User user) {
        assertNotNull(user);
        try{
            userService.remove(user);
        }catch (BusinessException be){
            assertEquals(be.getMessage(), resourceBundle.getString("be.user.remove"));
        }
        assertNotNull(user);
    }

    public void shouldRemoveUser(User user) {
        assertNotNull(user);
        userService.remove(user);
        user = userService.getDao().get(2L);
        assertNull(user);
    }
}
