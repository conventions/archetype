package org.conventions.archetype.test.at;

import org.conventions.archetype.test.ft.BasePage;
import org.conventionsframework.util.ResourceBundle;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.Locale;

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
            resourceBundle = new ResourceBundle("messages", Locale.ENGLISH);//force test locale, see test-faces-config

        } catch (Exception e) {
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
