package org.conventions.archetype.test.bdd;

import org.conventions.archetype.security.AppSecurityContext;
import org.conventions.archetype.service.UserService;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: rmpestano
 * Date: 10/31/13
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 *
 */
public class BaseStep implements Serializable{

    @Inject
    AppSecurityContext securityContext;

    @Inject
    UserService userService;

    protected void login(String username, String password) {
        if (securityContext == null) {//in client mode(RunAsClient) will be null
            return;
        }
        securityContext.setUserService(userService);//not allowed multiple extended persistenceContexts
        //they must use same persistence context cause userService has extended persistence context
        securityContext.doLogon(username,password);
    }


}
