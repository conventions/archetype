/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.security;

import org.conventions.archetype.model.User;
import org.conventionsframework.qualifier.LoggedIn;
import org.conventionsframework.qualifier.SecurityMethod;
import org.conventionsframework.security.BaseSecurityInterceptor;
import javax.interceptor.*;

import javax.inject.Inject;
import javax.interceptor.Interceptor;
import java.util.List;

@SecurityMethod
@Interceptor
public class SecurityInterceptor extends BaseSecurityInterceptor {

    @Inject
    @LoggedIn
    User user;



    //need by glassfish which doesnt handle interceptor inheritance
    @Override
    @AroundInvoke
    public Object checkPermission(InvocationContext ic) throws Exception {
        return super.checkPermission(ic);
    }
    
    
    

    /**
     * this method is responsible for deciding if current user has permission to
     * execute a method
     *
     * @param rolesAllowed roles passed in the method
     * @return true if user has permission, false otherwise
     */
    @Override
    public boolean checkUserPermissions(String[] rolesAllowed) {
        if (user == null || user.getId() == null || user.getGroups() == null || user.getGroups().isEmpty() || !userHasRoles()) {
            return false;
        }


        List<String> userRoles = user.getUserRoles();

        for (String role : rolesAllowed) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }



    private boolean userHasRoles() {
        return !user.getUserRoles().isEmpty();
    }
}
