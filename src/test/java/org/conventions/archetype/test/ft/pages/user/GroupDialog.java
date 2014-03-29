package org.conventions.archetype.test.ft.pages.user;

import org.conventions.archetype.test.ft.BasePage;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.CompositeAction;

import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/9/14.
 */
public class GroupDialog extends BasePage{

    @Root
    private GrapheneElement dialog;

    @FindByJQuery("button[id$=modal_bt]")
    private GrapheneElement btAdd;

    @FindByJQuery("tbody[id$=selectionTable_data]")
    private GrapheneElement groupsTable;

    @FindByJQuery("input[id*=colName]")
    private GrapheneElement filter;

    @Drone
    private WebDriver browser;



    public boolean isPresent(){
        return this.dialog.isPresent();
    }

    public GrapheneElement getGroupsTable() {
        return groupsTable;
    }


    public void addAllGroups() {
        List<WebElement> rows =  super.getTableRowsWithTDs("selectionTable_data");
         assertTrue(!rows.isEmpty());
        assertTrue(rows.size() == 2);
        CompositeAction ca = new CompositeAction();
        ca.addAction(new Actions(browser).keyDown(Keys.CONTROL).build());
        for (WebElement row : rows) {
            ca.addAction(new Actions(browser).click(row).build());
        }
        ca.addAction(new Actions(browser).keyUp(Keys.CONTROL).build());
        ca.perform();
        Graphene.waitGui();
        guardAjax(btAdd).click();

    }

    private void searchGroups(String name) {
        filter.clear();
        guardAjax(filter).sendKeys(name);
    }
}
