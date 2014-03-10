package org.conventions.archetype.test.ft.pages.common;

import org.conventions.archetype.test.util.TestMessageProvider;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rmpestano on 3/9/14.
 */
public class Menu {

    @Root
    private GrapheneElement menuBar;

    private GrapheneElement userMenu;

    private GrapheneElement groupMenu;

    private GrapheneElement roleMenu;


    private GrapheneElement getUserMenu() {
        if (userMenu == null) {
            userMenu = menuBar.findElement(By.linkText(TestMessageProvider.getMessage("user") + "s"));
        }
        return userMenu;
    }

    private GrapheneElement getGroupMenu() {
        if (groupMenu == null) {
            groupMenu = menuBar.findElement(By.linkText(TestMessageProvider.getMessage("groups")));
        }
        return groupMenu;
    }

    private GrapheneElement getRoleMenu() {
        if (roleMenu == null) {
            roleMenu = menuBar.findElement(By.linkText(TestMessageProvider.getMessage("roles")));
        }
        return roleMenu;
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

}
