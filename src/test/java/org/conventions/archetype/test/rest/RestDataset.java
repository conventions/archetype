package org.conventions.archetype.test.rest;

import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.User;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by rmpestano on 5/16/14.
 * Used to initialize rest test dataset
 */
@Singleton
@Startup
public class RestDataset {

    @PersistenceContext(unitName = "archetypeTestPU")
    EntityManager em;

    @Inject
    @Service
    BaseService<User> userService;


    @PostConstruct
    public void initRestDataSet(){
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

        User userWithGroup = new User().name("restUser").password("restUser");

        Group group = new Group("restGroup");
        userWithGroup.addGroup(group);
        userService.store(userWithGroup);
        userService.store(new User().name("restUser2").password("restUser2"));
    }
}
