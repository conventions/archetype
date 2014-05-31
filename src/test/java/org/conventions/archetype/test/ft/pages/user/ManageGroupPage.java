package org.conventions.archetype.test.ft.pages.user;

import org.conventions.archetype.test.ft.BasePage;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/10/14.
 */

@Location("/user/manageGroup.faces")
public class ManageGroupPage extends BasePage {

    @FindByJQuery("div[id$=findGroupDlg]")
    private GroupDialog groupDialog;

    @FindByJQuery("button[id$=btSave]")
    private GrapheneElement btSave;

    @FindByJQuery("button[id$=btManageGroup]")
    private GrapheneElement btManageGroup;

    @FindByJQuery("tbody[id$=groupTable_table_data]")
    private GrapheneElement selectedGroupsTable;


    public GrapheneElement getSelectedGroupsTable() {
        return selectedGroupsTable;
    }

    public void openGroupDialog(){
        btManageGroup.click();
        Graphene.waitAjax().until().element(groupDialog.getGroupsTable()).is().present();
    }

    public void selectGroups(){
        groupDialog.addAllGroups();
        Graphene.waitModel();
        List<WebElement> rows = super.getTableRows("groupTable");
        assertTrue(!rows.isEmpty());
        assertTrue(rows.size() == 6);
    }


}
