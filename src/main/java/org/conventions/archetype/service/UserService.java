/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.service;

import org.conventionsframework.service.BaseService;
import org.conventions.archetype.model.User;

/**
 *
 * @author rmpestano
 */
public interface UserService extends BaseService<User, Long> {


    
    User findUser(String username, String pass);

    void testPermission();
}
