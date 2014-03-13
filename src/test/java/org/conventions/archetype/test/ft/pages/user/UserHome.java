package org.conventions.archetype.test.ft.pages.user;

import org.conventions.archetype.test.ft.BasePage;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rmpestano on 3/10/14.
 */

@Location("/user/userHome.faces")
public class UserHome extends BasePage {

    @FindByJQuery("button[id$=btListUser]")
    private GrapheneElement btListUser;

    @FindByJQuery("button[id$=btNewUser]")
    private GrapheneElement btNewUser;

    @FindByJQuery("button[id$=bt-save]")
    private GrapheneElement btSave;

    @FindByJQuery("input[id$=inpTxt]")
    private GrapheneElement username;

    @FindByJQuery("input[id$=inpPass]")
    private GrapheneElement password;

    @FindByJQuery("div[id$=table]")
    private GrapheneElement datatable;

    @FindByJQuery("div[id$=panelAdd]")
    private GrapheneElement panel;

    @FindByJQuery("div[id$=footer]")
    private GrapheneElement footer;

    public GrapheneElement getBtListUser() {
        return btListUser;
    }

    public GrapheneElement getBtNewUser() {
        return btNewUser;
    }

    public GrapheneElement getDatatable() {
        return datatable;
    }

    public GrapheneElement getUsername() {
        return username;
    }

    public GrapheneElement getPassword() {
        return password;
    }

    public boolean isListPage() {
        return datatable.isPresent();
    }

    public boolean isEditPage() {
        return panel.isPresent() && panel.findElement(By.partialLinkText(TestMessageProvider.getMessage("edit"))).isPresent();
    }

    public void newUser(String name, String password){
        guardHttp(btNewUser).click();
        this.username.clear();
        this.username.sendKeys(name);
        this.password.clear();
        this.password.sendKeys(password);
        guardAjax(btSave).click();
        verifyMessage(TestMessageProvider.getMessage("user.create.message"));
    }

    public void manageGroups(){
        //TODO add all available groups to added user
    }

    public void goToList(){
         guardHttp(btListUser).click();
    }

}
