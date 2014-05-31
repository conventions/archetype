package org.conventions.archetype.test.ft.pages.role;

import org.conventions.archetype.test.ft.BasePage;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

/**
 * Created by rmpestano on 3/10/14.
 */

@Location("/role/roleHome.faces")
public class RoleHome extends BasePage {

    @FindByJQuery("div[id$=table]")
    private GrapheneElement datatable;

    @FindByJQuery("div[id$=panelAdd]")
    private GrapheneElement panel;

    @FindByJQuery("div[id$=footer]")
    private GrapheneElement footer;

    @FindByJQuery("input[id$=inpTxt]")
    private GrapheneElement inputName;

    @FindByJQuery("button[id$=btBack]")
    private GrapheneElement backButton;

    @FindByJQuery("input[id*=colName]")
    private GrapheneElement filterName;
    
    @FindByJQuery("button[id$=resetRole]")
    private GrapheneElement resetButton;

    @FindByJQuery("div[id$=role\\:comboOneMenu] > div.ui-selectonemenu-trigger")
    private GrapheneElement roleSelect;
    
    @FindByJQuery("div[id$=role\\:comboOneMenu]")
    private GrapheneElement roleSelectText;

    public GrapheneElement getDatatable() {
        return datatable;
    }

    public void newRole(String name){
    		gotoRoleForm();
            insertRole(name);
            guardAjax(backButton).click();
    }
    
    public void gotoRoleForm(){
    	  if(isListPage()){
              WebElement newButton = footer.findElement(By.xpath("//button"));
              newButton.click();
    	  }   
    }

    public void insertRole(String name) {
        inputName.clear();
        inputName.sendKeys(name);
        GrapheneElement btSave = panel.findElement(By.xpath("//button"));
        guardAjax(btSave).click();
        verifyMessage(resourceBundle.getString("role.create.message"));
    }

    public void filterByName(String query){
        filterName.clear();
        guardAjax(filterName).sendKeys(query);
    }

    public void filterByRoleFilter(String name){
        selectItem(roleSelect,name,true);
    }

    public boolean isListPage(){
        return datatable.isPresent();
    }

    public GrapheneElement getFooter() {
        return footer;
    }
    
    public void reset(){
    	guardAjax(resetButton).click();
    }

    public boolean isFormPage(){
       return panel.isPresent();
    }

    public GrapheneElement getRoleSelectText() {
		return roleSelectText;
	} 

    public GrapheneElement getInputName() {
		return inputName;
	}

}
