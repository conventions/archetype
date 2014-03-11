package org.conventions.archetype.test.ft;

import org.conventions.archetype.test.ft.pages.HomePage;
import org.conventions.archetype.test.ft.pages.common.Menu;
import org.conventions.archetype.test.ft.pages.group.GroupHome;
import org.conventions.archetype.test.ft.pages.role.RoleHome;
import org.conventions.archetype.test.ft.pages.user.UserHome;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/9/14.
 */
public class UserFT extends BaseFT {

    @FindByJQuery("div[id$=menuBar]")
    private Menu menu;

    @Page
    private UserHome userHome;

    @Page
    private RoleHome roleHome;

    @Page
    private GroupHome groupHome;

    @Test
    @InSequence(1)
    public void doLogon(@InitialPage HomePage home){
        assertTrue(home.getLogonDialog().isPresent());
        home.getLogonDialog().doLogon("admin", "admin");
        home.verifyMessage(TestMessageProvider.getMessage("logon.info.successful"));
    }

    @Test
    @InSequence(2)
    public void insertRole(){
       menu.gotoRoleHome();
       roleHome.newRole("test role");
    }
}
