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
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/9/14.
 */
public class UserFt extends BaseFt {

    @FindByJQuery("div[id$=menuBar]")
    private Menu menu;

    @Page
    private UserHome userHome;

    @Page
    private RoleHome roleHome;

    @Page
    private GroupHome groupHome;

    @Page
    private HomePage home;

    @Test
    @InSequence(1)
    public void shouldLogonWithSuccess(@InitialPage HomePage home){
        assertTrue(home.getLogonDialog().isPresent());
        home.getLogonDialog().doLogon("admin", "admin");
        home.verifyMessage(TestMessageProvider.getMessage("logon.info.successful"));
    }


    @Test
    @InSequence(2)
    public void shouldInsertRoleWithSuccess(){
       menu.gotoRoleHome();
       roleHome.newRole("test role");
       roleHome.newRole("test role2");
    }

    @Test
    @InSequence(3)
    public void shouldInsertGroupWithSuccess(){
        menu.gotoGroupHome();
        groupHome.newGroup("testgroup");
        groupHome.newGroup("another");
    }

    @Test
    @InSequence(4)
    public void shouldInsertUserWithSuccess(){
        menu.gotoUserHome();
        userHome.newUser("test user", "test");
        userHome.manageGroups();
    }


    @Test
    @InSequence(5)
    public void shouldSearchUserWithSuccess(){
        menu.gotoUserHome();
        userHome.goToList();
        userHome.filterByName("test user");
        assertEquals(userHome.getDatatable().findElements(By.xpath("//tbody//tr[@role='row']")).size(), 1);
        userHome.filterByName("zzzzx");
        assertTrue(userHome.getDatatable().findElements(By.xpath("//tbody//tr[@role='row']")).isEmpty());
    }

    @Test
    @InSequence(99)
    public void shouldLogoutSuccessfully(){
        menu.doLogout();
        assertTrue(home.getLogonDialog().isPresent());
    }
}
