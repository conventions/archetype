package org.conventions.archetype.test.ft.pages.group;

import org.conventions.archetype.test.ft.BasePage;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Random;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

/**
 * Created by rmpestano on 3/10/14.
 */

@Location("/group/groupHome.faces")
public class GroupHome extends BasePage {

    @FindByJQuery("div[id$=table]")
    private GrapheneElement datatable;

    @FindByJQuery("div[id$=panelAdd]")
    private GrapheneElement panel;

    @FindByJQuery("div[id$=footer]")
    private GrapheneElement footer;

    @FindByJQuery("input[id$=inpTxt]")
    private GrapheneElement inputName;

    @FindByJQuery(".ui-picklist-source")
    private GrapheneElement picklistSource;

    @FindByJQuery("button[id$=btBack]")
    private GrapheneElement btBack;

    @Drone
    private WebDriver browser;


    public GrapheneElement getDatatable() {
        return datatable;
    }

    public void newGroup(String name) {
        if (isListPage()) {
            WebElement newButton = footer.findElement(By.xpath("//button"));
            newButton.click();
            inputName.clear();
            inputName.sendKeys(name);
            GrapheneElement btSave = panel.findElement(By.xpath("//button"));
            selectRoles();
            guardAjax(btSave).click();
            verifyMessage(resourceBundle.getString("group.create.message"));
            guardAjax(btBack).click();
        }
    }

    public boolean isListPage() {
        return datatable.isPresent();
    }

    public boolean isEditPage() {
        return panel.isPresent() && panel.findElement(By.partialLinkText(resourceBundle.getString("edit"))).isPresent();
    }

    public void selectRoles() {
        Actions actions = new Actions(browser);
        List<WebElement> roles = picklistSource.findElements(By.xpath("//ul//li[contains(@class,'ui-picklist-item')]"));
        actions.dragAndDrop(roles.get(new Random().nextInt(roles.size())), browser.findElement(By.className("ui-picklist-target"))).perform();
    }

    public void sortGroupsByName(){
        WebElement column = getTableColumnById("name");
        guardAjax(column).click();
    }

    public List<WebElement> getTableRows() {
        return super.getTableRows("table");
    }

    public List<WebElement> getTableRowsWithTDa() {
        return super.getTableRowsWithTDs("table");
    }
}
