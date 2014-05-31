package org.conventions.archetype.test.ft.pages.common;

import org.conventions.archetype.test.ft.BasePage;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rmpestano on 3/9/14.
 */
public class Menu extends BasePage{

    @Root
    private GrapheneElement menuBar;

    private WebElement userMenu;

    private WebElement groupMenu;

    private WebElement roleMenu;

    private WebElement logoutMenu;


    private WebElement getUserMenu() {
        if (userMenu == null) {
            userMenu = findItemByText(resourceBundle.getString("user") + "s");
        }
        return userMenu;
    }

    private WebElement getGroupMenu() {
        if (groupMenu == null) {
            groupMenu = findItemByText(resourceBundle.getString("groups"));
        }
        return groupMenu;
    }

    private WebElement getRoleMenu() {
        if (roleMenu == null) {
            roleMenu = findItemByText(resourceBundle.getString("roles"));
        }
        return roleMenu;
    }

    public WebElement getLogoutMenu() {
        if(logoutMenu == null){
            logoutMenu = findItemByText(resourceBundle.getString("logout"));
        }
        return logoutMenu;
    }

    public void gotoUserHome() {
        guardHttp(getUserMenu()).click();
    }

    public void gotoGroupHome() {
        guardHttp(getGroupMenu()).click();
    }

    public void gotoRoleHome() {
        guardHttp(getRoleMenu()).click();
    }


    private WebElement findItemByText(String text){
        return menuBar.findElement(By.xpath("//span[@class='ui-menuitem-text' and text() = '" + text +"']"));
    }

    public void doLogout() {
        guardHttp(getLogoutMenu()).click();
    }

    public boolean isPresent(){
        return menuBar.isPresent();
    }

}
