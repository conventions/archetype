package org.conventions.archetype.test.util;

import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.model.User;
import org.conventions.archetype.util.AppConstants;
import org.conventions.archetype.util.Utils;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmpestano on 3/9/14.
 *
 * used by functional tests as dbunit cant help (yet) on black boxed tests
 */
@Stateless
@Named
public class TestService implements Serializable {

    @PersistenceContext(unitName = "archetypeTestPU")
    EntityManager em;

    public void initDatabase(){
        clearDatabase();
        User admin = new User();
        admin.setName("admin");
        admin.setPassword(new Utils().encrypt("admin"));
        List<Group> groups = new ArrayList<Group>();
        List<Role> roles = new ArrayList<Role>();
        Role  roleAdmin = new Role();
        roleAdmin.setName(AppConstants.Role.ADMIN);
        roles.add(roleAdmin);
        Group group = new Group();
        group.setName("admins");
        group.setRoles(roles);
        admin.setGroups(groups);
        em.persist(admin);
        em.flush();

        User user = new User();
        user.setName("user");
        user.setPassword(new Utils().encrypt("user"));
        em.persist(user);
        em.flush();

    }

    public void clearDatabase() {
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
