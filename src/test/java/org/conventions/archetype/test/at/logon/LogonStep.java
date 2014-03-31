package org.conventions.archetype.test.at.logon;

import org.conventions.archetype.test.at.BaseAtStep;
import org.conventions.archetype.test.ft.pages.HomePage;
import org.conventions.archetype.test.ft.pages.common.Menu;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;

import java.io.Serializable;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: rmpestano
 * Date: 10/31/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogonStep extends BaseAtStep implements Serializable {

  @Drone
  private WebDriver       browser;

  @ArquillianResource
  private URL             baseUrl;

  @FindByJQuery("div[id$=menuBar]")
  private Menu menu;

  @Page
  private HomePage home;


  @Given("i am at logon screen")
  public void imAtLogon() {
    if (!home.getLogonDialog().isPresent()) {
      //if is already logged in, do logout
        super.goToPage(home);
      }
  }

  @When("i logon providing credentials $username, $password")
  public void loginWithCredentials(String username, String password) {
    home.getLogonDialog().doLogon(username,password);
  }

  @Then("i should be logged in")
  public void shouldBeAt() {
    home.verifyMessage(TestMessageProvider.getMessage("logon.info.successful"));
  }

}
