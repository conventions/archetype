/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.converter;

import org.conventions.archetype.model.Role;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.apache.myfaces.extensions.cdi.core.api.Advanced;
import org.conventionsframework.converter.AbstractBaseConverter;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;
import org.conventionsframework.service.impl.BaseServiceImpl;
import org.conventions.archetype.service.RoleService;

/**
 *
 * @author rmpestano
 */

@Advanced
@FacesConverter(value="roleConverter")
public class RoleConverter extends AbstractBaseConverter{
    
    @Inject
    public void setService(RoleService service){
        super.setBaseService(service);
    }
    
}
