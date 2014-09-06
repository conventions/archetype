/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventions.archetype.converter;

import org.conventions.archetype.model.Role;
import org.conventionsframework.converter.BaseConverter;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * @author rmpestano
 */
@FacesConverter(value="roleConverter")
public class RoleConverter extends BaseConverter {


    @Inject
    public void setService(@Service BaseService<Role> baseService) {
        super.setBaseService(baseService);
    }

}
