/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.service;

import org.conventions.archetype.bean.ComboMBean;
import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.util.AppConstants;
import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.service.impl.BaseServiceImpl;
import org.conventionsframework.util.ResourceBundle;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.persistence.Query;
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

    @Inject
    ResourceBundle resourceBundle;
    
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

    @Override
    public void beforeRemove(Role entity) {
        if(entity.getName().equals(AppConstants.Role.ADMIN)){
            throw new BusinessException(resourceBundle.getString("role.be.admin"));
        }
        if(roleIsAssociatedWithGroups(entity)){
            throw new BusinessException(resourceBundle.getString("role.be.groups"));
        }
    }

    private boolean roleIsAssociatedWithGroups(Role entity) {
        Query q = getEntityManager().createNamedQuery("Group.findGroupsWithrole");
        q.setParameter("roleId",entity.getId());
        List<Group> groupsFound = q.getResultList();
        return !groupsFound.isEmpty();
    }
}
