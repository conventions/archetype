package org.conventions.archetype.test.ft.pages.user;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * Created by rmpestano on 4/27/14.
 */
public class SearchUserDialog {

    @Root
    private GrapheneElement dialog;

    @FindByJQuery("input[id$=group]")
    private GrapheneElement group;

    @FindByJQuery("button[id$=search]")
    private GrapheneElement searchButton;

    @Drone
    private WebDriver browser;


    public void searchByGroup(String groupName){
        group.clear();
        waitModel();
        group.sendKeys(groupName);
        guardAjax(searchButton).click();
    }




}
