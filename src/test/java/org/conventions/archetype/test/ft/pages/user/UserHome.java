package org.conventions.archetype.test.ft.pages.user;

import org.conventions.archetype.test.ft.BasePage;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;

/**
 * Created by rmpestano on 3/10/14.
 */

@Location("/user/userHome.faces")
public class UserHome extends BasePage {

    @FindByJQuery("button[id$=btListUser]")
    private GrapheneElement btListUser;

    @FindByJQuery("button[id$=btNewUser]")
    private GrapheneElement btNewUser;

    public GrapheneElement getBtListUser() {
        return btListUser;
    }

    public GrapheneElement getBtNewUser() {
        return btNewUser;
    }
}
