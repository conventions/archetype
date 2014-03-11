package org.conventions.archetype.test.ft.pages.role;

import org.conventions.archetype.test.ft.BasePage;
import org.conventions.archetype.test.util.TestMessageProvider;
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


    public GrapheneElement getDatatable() {
        return datatable;
    }

    public void newRole(String name){
        if(isListPage()){
            WebElement newButton = footer.findElement(By.xpath("//button"));
            newButton.click();
            inputName.clear();
            inputName.sendKeys(name);
            GrapheneElement btSave = panel.findElement(By.xpath("//button"));
            guardAjax(btSave).click();
            verifyMessage(TestMessageProvider.getMessage("role.create.message"));

        }
    }

    public boolean isListPage(){
        return datatable.isPresent();
    }

    public boolean isEditPage(){
       return panel.isPresent() && panel.findElement(By.partialLinkText(TestMessageProvider.getMessage("edit"))).isPresent();
    }


}
