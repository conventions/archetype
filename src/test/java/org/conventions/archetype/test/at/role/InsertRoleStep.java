package org.conventions.archetype.test.at.role;

import junit.framework.Assert;
import org.conventions.archetype.test.ft.pages.common.Menu;
import org.conventions.archetype.test.ft.pages.role.RoleHome;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA. User: rmpestano Date: 10/31/13 Time: 8:49 PM To
 * change this template use File | Settings | File Templates.
 */
public class InsertRoleStep implements Serializable {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL baseUrl;

    @Page
    private RoleHome roleHome;

    @FindByJQuery("div[id$=menuBar]")
    private Menu menu;


    @Given("user go to role home")
    public void goToRoleHome() {
        menu.gotoRoleHome();
    }

    @Given("user clicks in new button")
    public void userClickInNewButton() {
        WebElement footer = roleHome.getFooter();
        assertTrue(footer.isDisplayed());
        WebElement newButton = footer.findElement(By.xpath("//button"));
        assertTrue(newButton.isDisplayed());
        guardHttp(newButton).click();
    }

    @Then("should insert role with name $name")
    public void shouldInsertRole(String name) {
        Assert.assertTrue(roleHome.isFormPage());
        roleHome.insertRole(name);

    }

}
