/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.bean;

import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rmpestano
 */
@Named
@ApplicationScoped
public class ComboMBean implements Serializable {

    private List<SelectItem> groupList;
    private List<SelectItem> roleList;
    
    @Inject
    @Service        
    BaseService<Group,Long> groupService;
    
    @Inject
    @Service   
    BaseService<Role,Long> roleService;

    
    public List<SelectItem> getGroupList() {
        if (groupList == null) {
            groupList = new ArrayList<SelectItem>();
            groupList.add(new SelectItem(null, "All"));
            List<Group> allGroups = groupService.getDao().findAll();
            for (Group group : allGroups) {
                SelectItem item = new SelectItem(group.getName(), group.getName());
                groupList.add(item);
            }
        }
        return groupList;
    }
    
    public List<SelectItem> getRoleList() {
         if (roleList == null) {
            roleList = new ArrayList<SelectItem>();
            roleList.add(new SelectItem(null, "All"));
            List<Role> allGroups = roleService.getDao().findAll();

            for (Role role : allGroups) {
                SelectItem item = new SelectItem(role.getName(), role.getName());
                roleList.add(item);
            }
        }
        return roleList;
    }
    
    public void updateGroupList(){
        groupList = null;//force update
    }

    public void updateRoleList(){
        roleList = null;//force update
    }
}
