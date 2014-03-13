package org.conventions.archetype.test.ft.pages.group;

import org.conventions.archetype.test.ft.BasePage;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

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

    @Drone
    WebDriver browser;


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
            verifyMessage(TestMessageProvider.getMessage("group.create.message"));
        }
    }

    public boolean isListPage() {
        return datatable.isPresent();
    }

    public boolean isEditPage() {
        return panel.isPresent() && panel.findElement(By.partialLinkText(TestMessageProvider.getMessage("edit"))).isPresent();
    }

    public void selectRoles() {
        Actions actions = new Actions(browser);
        List<WebElement> roles = picklistSource.findElements(By.xpath("//li[contains(@class,'ui-picklist-item')]"));
        for (WebElement role : roles) {
            actions.dragAndDrop(role,browser.findElement(By.className("ui-picklist-target"))).perform();
        }
    }


}
