package org.conventions.archetype.service;

import org.conventions.archetype.model.Group;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.service.impl.BaseServiceImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Map;

/**
 * Created by rmpestano on 5/18/14.
 */
@Stateless
public class GroupModalService extends BaseServiceImpl<Group> {


    @Override
    public Criteria configPagination(SearchModel<Group> searchModel) {
        Map<String, Object> searchFilter = searchModel.getFilter();
        Criteria dc = getCriteria();
        //not show in group popup groups that user being edited already have
        List<Long> userGroups = (List<Long>) searchFilter.get("userGroups");//@see UserMBean#preLoadDialog()
        if (userGroups != null && !userGroups.isEmpty()) {
            dc.add(Restrictions.not(Restrictions.in("id", userGroups)));
        }
        return super.configPagination(searchModel,dc);
    }
}
