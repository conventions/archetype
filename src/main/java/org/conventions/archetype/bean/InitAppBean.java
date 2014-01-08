/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.bean;

import org.conventionsframework.qualifier.Config;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;
import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.model.User;
import org.conventions.archetype.util.Utils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 *
 * @author rmpestano Oct 2, 2011 11:48:37 AM
 */
@Named
@ApplicationScoped
public class InitAppBean implements Serializable{
    
    @Inject
    @Service
    BaseService<User,Long> userService;

    @Inject
    @Log
    transient Logger log;
    
    @Inject 
    Utils utils;

    @Inject
    @Config
    Double myDoubleConfig;


    @PostConstruct
    public void init() {
        /**
         * @see org.conventions.archetype.configuration.AppConfigProvider
         */
        log.info("my double config:" + myDoubleConfig);
        log.info("init app mbean");
        if(userService.getDao().countAll() == 0){
            final Group groupManager = new Group("Manager");
            final Group groupDev = new Group("Devs");
            final Group groupDevOps = new Group("DevOps");
            final Group groupOperations = new Group("Operations");
            
            Role roleOp = new Role("Operator"); 
            Role roleSystem = new Role("System Engineer"); 
            Role roleArch = new Role("architect"); 
            
            groupDevOps.addRole(roleSystem);
            groupDevOps.addRole(roleArch);
            
            groupOperations.addRole(roleOp);
            groupOperations.addRole(roleSystem);
            
            Role roleAdmin = new Role("Administrator"); 
            
            groupManager.addRole(roleAdmin);
            
            Role roleDev = new Role("Developer"); 
            
            groupDev.addRole(roleDev);
            groupDev.addRole(roleArch);
             
            User userAdmin = new User("admin");
            userAdmin.setPassword(utils.encrypt("admin"));
            userAdmin.addGroup(groupManager);
            userAdmin.addGroup(groupDevOps);
            userAdmin.addGroup(groupOperations);
            userAdmin.addGroup(groupDev);
            userService.store(userAdmin);
            
            User op = new User("Operator");
            op.setPassword(utils.encrypt("operator"));
            op.addGroup(groupOperations);
            userService.store(op);
            
            User devOps = new User("DevOps Man");
            devOps.setPassword(utils.encrypt("devops"));
            devOps.addGroup(groupDevOps);
            userService.store(devOps);
            
            User developer = new User("developer");
            developer.setPassword(utils.encrypt("developer"));
            developer.addGroup(groupDev);
            userService.store(developer);
            
            User guest = new User("guest");
            guest.setPassword(utils.encrypt("guest"));
            userService.store(guest);
        }
    }

}
