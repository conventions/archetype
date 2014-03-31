/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.service;

import org.conventions.archetype.model.Role;
import org.conventionsframework.service.BaseService;
import org.conventions.archetype.model.User;

import java.util.List;

/**
 *
 * @author rmpestano
 */
public interface UserService extends BaseService<User, Long> {


    
    User findUser(String username, String pass);

    void testPermission();

    List<User> findUserByRole(Role role);
}
