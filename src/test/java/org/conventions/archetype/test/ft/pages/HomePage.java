package org.conventions.archetype.test.ft.pages;

import org.conventions.archetype.test.ft.BasePage;
import org.conventions.archetype.test.ft.pages.common.LogonDialog;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;

/**
 * Created by rmpestano on 3/9/14.
 */
@Location("home.faces")
public class HomePage extends BasePage{


    @FindByJQuery("div[id$=logonDialog]")
    private LogonDialog logonDialog;


    public LogonDialog getLogonDialog() {
        return logonDialog;
    }


}
