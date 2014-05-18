/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.service;

import org.conventions.archetype.event.UpdateList;
import org.conventions.archetype.model.Group;
import org.conventions.archetype.model.Role;
import org.conventions.archetype.qualifier.ListToUpdate;
import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.service.impl.BaseServiceImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author rmpestano
 */
@Named("groupService")
@Stateful
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

    @Inject
    private RoleService roleService;

    @Inject
    private ResourceBundle resourceBundle;

    @Inject
    @ListToUpdate(ListToUpdate.ListType.GROUP)
    Event<UpdateList> updateGroupList;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager em;


    /**
     * override default entityManager which is type = TRANSACTION
     */
    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public DetachedCriteria configPagination(SearchModel<Group> searchModel) {

        DetachedCriteria dc = getDetachedCriteria();
        Group searchEntity = searchModel.getEntity();
        Map<String, String> tableFilter = searchModel.getDatatableFilter();//primefaces column filter
        if (tableFilter != null && !tableFilter.isEmpty()) {
            String role = tableFilter.get("roles");
            if (role != null && !"all".equalsIgnoreCase(role)) {
                //create join to fetch group roles 
                dc.createAlias("roles", "roles");
                dc.add(Restrictions.eq("roles.name", role));
            }
        }
        Map<String, Object> searchFilter = searchModel.getFilter();

        Long userId = (Long) searchFilter.get("currentUser");
        if (userId != null) {
            //create join to load groups of current user being edited
            dc.createAlias("users", "users");
            dc.add(Restrictions.eq("users.id", userId));
        }


        //you can return only your populated criteria(dc) 
        //but you can also pass your criteria to superclass(as below)
        //so the framework will add an ilike to string fields and eq to Integer/Long/Date/Calendar ones 
        //if those they are present in filters
        return super.configPagination(searchModel, dc);

    }


    /**
     * this method should be in a service that manage roles but we(purposely)
     * did not provided one so we do that here in groupService
     *
     * @param groupId
     * @return roles that are not in current group
     */
    @Override
    public List<Role> findAvailableRoles(Group group) {


        if (group.getRoles() == null || group.getRoles().isEmpty()) {
            return roleService.getDao().findAll();//if no roles associated all roles are available
        }

        List<Long> roleIds = new ArrayList<Long>();
        for (Role role : group.getRoles()) {
            roleIds.add(role.getId());
        }

        Query q = this.getEntityManager().createNamedQuery("Role.findRoleNotInGroup");
        q.setParameter("roleIds", roleIds);

        return q.getResultList();
        //Same as criteria below
//        DetachedCriteria dc = roleService.getDetachedCriteria();
//        dc.add(Restrictions.not(Restrictions.in("id", roleIds)));
//        return roleService.findByCriteria(dc);
    }

    @Override
    public void beforeRemove(Group entity) {
        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            throw new BusinessException(resourceBundle.getString("group.business01"));
        }
        if (entity.getUsers() != null && !entity.getUsers().isEmpty()) {
            throw new BusinessException(resourceBundle.getString("group.business02"));
        }
    }

    @Override
    public void afterStore(Group entity) {
        updateGroupList.fire(new UpdateList());


    }

    @Override
    public void afterRemove(Group entity) {
        updateGroupList.fire(new UpdateList());
    }


    @Override
    public void beforeStore(Group entity) {
        //override to perform logic before storing an entity
        if (isExistingGroup(entity)) {
            throw new BusinessException(resourceBundle.getString("group.business03"));
        }
    }


    private boolean isExistingGroup(Group group) {
        if (group == null) {
            return false;
        }
        DetachedCriteria dc = getDetachedCriteria();
        //used to ignore user id which we are editing
        if (group.getId() != null) {
            dc.add(Restrictions.ne("id", group.getId()));
        }

        if (!"".endsWith(group.getName())) {
            dc.add(Restrictions.ilike("name", group.getName(), MatchMode.EXACT));
            return (dao.getRowCount(dc) > 0);
        }
        return false;
    }

}
