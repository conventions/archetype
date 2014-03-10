package org.conventions.archetype.test.ft.pages.common;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rmpestano on 3/9/14.
 */
public class LogonDialog {

    @Root
    private GrapheneElement dialog;

    @FindByJQuery("input[id$=inptUser]")
    private GrapheneElement username;

    @FindByJQuery("input[id$=inptPass]")
    private GrapheneElement password;

    @FindByJQuery("button[id$=btLogon]")
    private GrapheneElement btLogon;


    public void doLogon(String username, String password){
        this.username.clear();
        this.username.sendKeys(username);
        this.password.clear();
        this.password.sendKeys(password);
        guardHttp(btLogon).click();
    }

    public boolean isPresent(){
        return this.username.isPresent();
    }

    public GrapheneElement getUsername() {
        return username;
    }

    public GrapheneElement getPassword() {
        return password;
    }

    public GrapheneElement getBtLogon() {
        return btLogon;
    }
}
