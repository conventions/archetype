package org.conventions.archetype.test.at;

import org.conventions.archetype.test.ft.BasePage;
import org.conventionsframework.util.ResourceBundle;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rmpestano on 3/30/14.
 */
public abstract class BaseAtStep {

    @Drone
    WebDriver browser;

    @ArquillianResource
    protected URL baseUrl;

    protected ResourceBundle resourceBundle;

    @FindByJQuery("a[id$=logout]")
    protected GrapheneElement logoutButton;

    protected BaseAtStep() {
        try {
            //when running as client(black box) bundle must be found in application classpath instead of current thread(test thread will look into test resources)
            resourceBundle = new ResourceBundle(getClass().getResourceAsStream("/messages_en.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logout() {
        guardHttp(logoutButton).click();
    }

    public void goToPage(BasePage page) {
        //Graphene.goTo(page.getClass());
        browser.get(baseUrl.toString() + page.getLocation());
    }
}
