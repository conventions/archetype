package org.conventions.archetype.test.util;

import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.model.User;
import org.conventions.archetype.service.RoleService;
import org.conventions.archetype.util.Utils;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by rmpestano on 3/9/14.
 *
 * used by functional tests as dbunit cant help (yet) on black boxed tests
 * also used by acceptance and BDD tests as dbunit and jbehave doesnt integrate yet
 */
@Stateless
@Named
public class TestService implements Serializable {

    @Inject
    @Service
    BaseService<User,Long> userService;

    @Inject
    RoleService roleService;

    @Inject
    @Service
    BaseService<Group,Long> groupService;

    @Inject
    Utils utils;

    public void initDatabase(){
        clearDatabase();
        User admin = new User();
        admin.setName("admin");
        admin.setPassword(new Utils().encrypt("admin"));
        userService.store(admin);

        User user = new User();
        user.setName("user");
        user.setPassword(new Utils().encrypt("user"));
        userService.store(user);
    }

    /**
     * init database with following entities
     *
     * User admin
     * |
     * ----- Groups: Manager(administrator), Devs(developer, architect)
     *
     * User developer
     * |
     * ----- Groups: Devs(developer, architect,groupWithoutRole)
     *
     * Group: groupWithoutRole
     * Group: groupWithoutUser
     *
     *
     */
    public void initDatabaseWithUserAndGroups(){
        clearDatabase();

        //groups
        final Group groupManager = new Group("Manager");
        final Group groupDev = new Group("Devs");
        final Group groupWithoutRole = new Group("groupWithoutRole");
        final Group groupWithoutUserAndRole = new Group("groupWithoutUserAndRole");


        //roles
        Role roleArch = new Role("architect");
        Role roleAdmin = new Role("administrator");
        Role roleDev = new Role("developer");
        Role simpleRole = new Role("simpleRole");

        roleService.store(simpleRole);

        groupManager.addRole(roleAdmin);


        groupDev.addRole(roleDev);
        groupDev.addRole(roleArch);

        User userAdmin = new User("admin");
        userAdmin.setPassword(utils.encrypt("admin"));
        userAdmin.addGroup(groupManager);
        userAdmin.addGroup(groupDev);
        userService.store(userAdmin);


        User developer = new User("developer");
        developer.setPassword(utils.encrypt("developer"));
        developer.addGroup(groupDev);
        developer.addGroup(groupWithoutRole);
        userService.store(developer);

        groupService.store(groupWithoutUserAndRole);
    }

    public void clearDatabase() {
        EntityManager em = userService.getEntityManager();
        em.createNativeQuery("delete from group__role_").executeUpdate();
        em.flush();
        em.createNativeQuery("delete from user__group_").executeUpdate();
        em.flush();
        em.createNativeQuery("delete from role_").executeUpdate();
        em.flush();
        em.createNativeQuery("delete from group_").executeUpdate();
        em.flush();
        em.createNativeQuery("delete from user_").executeUpdate();
        em.flush();
    }
}
