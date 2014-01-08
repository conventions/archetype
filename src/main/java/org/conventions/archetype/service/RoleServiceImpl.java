/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.service;

import org.conventionsframework.service.impl.BaseServiceImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.conventions.archetype.bean.ComboMBean;
import org.conventions.archetype.model.Role;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rmpestano
 *
 * NON EJB Service
 */
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    @Inject
    ComboMBean comboMBean;
    
    @Override
    public DetachedCriteria configFindPaginated(Map<String, String> columnFilters, Map<String, Object> externalFilter) {
        
        DetachedCriteria dc = getDetachedCriteria();
        
        List<Long> groupRoles = (List<Long>) externalFilter.get("groupRoles");
        if(groupRoles != null && !groupRoles.isEmpty()){
            //open roleModal listing only with not used groups
            //PS: not used cause roleAssociation is done with picklist now not with list of values anymore
            dc.add(Restrictions.not(Restrictions.in("id", groupRoles)));
        }
        return super.configFindPaginated(columnFilters, externalFilter,dc); 
    }

    @Override
    public void afterStore(Role entity) {
        comboMBean.updateRoleList();
    }

    @Override
    public void afterRemove(Role entity) {
         comboMBean.updateRoleList();
    }
    
    

    
}
