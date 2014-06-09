package org.conventions.archetype.test.ft;

import org.conventions.archetype.test.ft.pages.HomePage;
import org.conventions.archetype.test.ft.pages.common.Menu;
import org.conventions.archetype.test.ft.pages.group.GroupHome;
import org.conventions.archetype.test.ft.pages.role.RoleHome;
import org.conventions.archetype.test.ft.pages.user.UserHome;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/9/14.
 */
public class ArchetypeFt extends BaseFt {

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
        home.verifyMessage(resourceBundle.getString("logon.info.successful"));
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
    public void shouldClearRole(){
    	roleHome.gotoRoleForm();
    	GrapheneElement name = roleHome.getInputName();
    	name.sendKeys("role name");
    	Graphene.waitGui();
    	roleHome.reset();
    	assertTrue(name.getText().isEmpty());
    }

    @Test
    @InSequence(4)
    public void shouldFilterRoles(){
        menu.gotoRoleHome();
        roleHome.filterByRoleFilter("test role");
        List<WebElement> rows = roleHome.getTableRows("table");
        assertNotNull(rows);
        assertEquals(rows.size(),1);
        assertTrue(rows.get(0).getText().contains("test role"));
    }
    
    @Test
    @InSequence(5)
    public void roleFilterShouldBeKeeptInSession(){
        menu.gotoUserHome();
        menu.gotoRoleHome();
        assertEquals(roleHome.getRoleSelectText().getText(), "test role");
        List<WebElement> rows = roleHome.getTableRows("table");
        assertNotNull(rows);
        assertEquals(rows.size(),1);
        assertTrue(rows.get(0).getText().contains("test role"));
    }


    @Test
    @InSequence(6)
    public void shouldInsertGroupWithSuccess(){
        menu.gotoGroupHome();
        groupHome.newGroup("testgroup");
        groupHome.newGroup("Another");
    }

    @Test
    @InSequence(7)
    public void shouldSortGroupsByName(){
        menu.gotoGroupHome();
        groupHome.sortGroupsByName();
        List<WebElement> rows = groupHome.getTableRows();
        assertNotNull(rows);
        assertTrue(rows.get(0).getText().contains("Another"));
        groupHome.sortGroupsByName();
        assertTrue(rows.get(0).getText().contains("testgroup"));
    }

    @Test
    @InSequence(8)
    public void shouldFilterGroupsByRole(){
        groupHome.filterInputColumn("name","testgroup");
        assertEquals(1,groupHome.getTableRows().size());

    }

    @Test
    @InSequence(9)
    public void shouldInsertUserWithSuccess(){
        menu.gotoUserHome();
        userHome.newUser("test user", "test");
        userHome.manageGroups();
    }


    @Test
    @InSequence(10)
    public void shouldSearchUserWithSuccess(){
        menu.gotoUserHome();
        userHome.goToList();
        userHome.filterByName("test user");
        assertEquals(userHome.getDatatable().findElements(By.xpath("//tbody//tr[@role='row']")).size(), 1);
        userHome.filterByName("zzzzx");
        assertTrue(userHome.getDatatable().findElements(By.xpath("//tbody//tr[@role='row']")).isEmpty());
    }

    @Test
    @InSequence(11)
    public void shouldSearchUserByGroup(){
        userHome.searchByGroup("Manager");
        List<WebElement> rows = userHome.getTableRows("table");
        assertEquals(rows.size(),2);
    }


    @Test
    @InSequence(99)
    public void shouldLogoutSuccessfully(){
        menu.doLogout();
        assertTrue(home.getLogonDialog().isPresent());
    }

    @Test
    @InSequence(100)
    public void shouldLogonAndRedirectToGroupHome(){
       goToPage(groupHome);
       String expiredText = browser.findElement(By.xpath("//span[contains(@class,'ui-messages-warn-summary')]")).getText();
       assertEquals("Your session has expired, please logon again",expiredText);
       home.getLogonDialog().doLogon("admin","admin");
       assertTrue(groupHome.isListPage());
    }
}
