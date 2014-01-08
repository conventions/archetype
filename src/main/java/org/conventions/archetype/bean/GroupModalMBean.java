/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.bean;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.WindowScoped;
import org.conventionsframework.bean.ModalMBean;
import org.conventionsframework.qualifier.Service;
import org.primefaces.event.CloseEvent;
import org.conventions.archetype.model.Group;
import org.conventions.archetype.service.GroupService;
import org.conventions.archetype.service.GroupServiceImpl;

import javax.inject.Named;
import java.io.Serializable;

 /**
 * 
 * @author rmpestano
 * provides group list of values
 */
@WindowScoped
@Named(value = "groupModalMBean")
@Service(GroupService.class)
public class GroupModalMBean extends ModalMBean<Group> implements Serializable {

    private Group[] selectedGroups;
    
    public GroupModalMBean() {
    }

    public Group[] getSelectedGroups() {
        return selectedGroups;
    }

    public void setSelectedGroups(Group[] selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    

    @Override
    public Object modalCallback() {
       return selectedGroups;
    }
  
    public void clearSelection(CloseEvent event){
        this.selectedGroups = null;
    }
    
    /**
     * pass parameter to service so only groups which 
     * are no associated with user will be displayed in the popup
     * @see GroupServiceImpl#configFindPaginated(java.util.Map, java.util.Map) 
     */
    @Override
    public void onOpen() {
        //parameters is populated via CDI event(ModalInitEvent) @see UserMBean#preLoadDialog()
        getPaginator().setFilter(getParameters());//substitute paginator filter with parammeters filter
    }
    
    
}