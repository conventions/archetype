package org.conventions.archetype.test.ft;

import junit.framework.Assert;
import org.conventionsframework.util.ResourceBundle;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;


public abstract class BasePage implements Serializable {

    @Drone
    private WebDriver browser;

    protected ResourceBundle resourceBundle;

    protected BasePage() {
        try {
            resourceBundle = new ResourceBundle("messages", Locale.ENGLISH);//force test locale, see test-faces-config

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //navigation

    public String getLocation() {
        if (!getClass().isAnnotationPresent(Location.class)) {
            throw new RuntimeException("Provide @Location annotation for class:" + getClass().getSimpleName());
        }
        Location location = getClass().getAnnotation(Location.class);
        return location.value();
    }

    //messages

    @FindByJQuery("div.ui-growl-message")
    private GrapheneElement growl;

    public void verifyMessage(String msg) {
        Graphene.waitAjax().until().element(growl).is().present();
        Assert.assertEquals(msg.trim(), growl.getText().trim());
    }


    //datable
    public List<WebElement> getTableRows(String tableId){
        return browser.findElements(By.xpath("//tbody[contains(@id,'" +tableId +"')]//tr[@role='row']"));
    }

    public List<WebElement> getTableRowsWithTDs(String tableId){
        return browser.findElements(By.xpath("//tbody[contains(@id,'" +tableId +"')]//tr[@role='row']//td[@role='gridcell']"));
    }

    public WebElement getTableColumnById(String colId){
        WebElement column = browser.findElement(By.xpath("//th[contains(@class,'ui-filter-column') and contains(@id,'" + colId + "')]"));
        return column;
    }

    public void filterInputColumn(String colId, String query) {
        WebElement column = browser.findElement(By.xpath("//th[contains(@class,'ui-filter-column') and contains(@id,'" + colId + "')]//input"));
        guardAjax(column).sendKeys(query);
    }

    public void clearFilterColumn(String colId){
        WebElement column = browser.findElement(By.xpath("//th[contains(@class,'ui-filter-column') and contains(@id,'" + colId + "')]//input"));
        guardNoRequest(column).clear();
    }

    public void filterSelectColumn(String colId, String query) {
        StringBuilder xpath = new StringBuilder("//th[contains(@class,'ui-filter-column') and contains(@id,'" + colId + "')]");
        xpath.append("//div[contains(@class,'ui-selectonemenu-trigger')]//span[contains(@class,'ui-icon-triangle-1-s')]");
        browser.findElement(By.xpath(xpath.toString())).click();
        Graphene.waitAjax().until().element(By.className("ui-selectonemenu-items-wrapper")).is().present();
        WebElement selectItem = browser.findElement(By.xpath("//li[contains(@class,'ui-selectonemenu-item') and contains(text(),'" + query +"')]"));
        guardAjax(selectItem).click();
    }


    //select
    protected void selectItem(GrapheneElement selectOne, String descricao) {
        this.selectItem(selectOne, descricao, false);
    }

    protected void selectItem(GrapheneElement selectOne, String descricao, boolean hasListener) {
        Graphene.waitModel().until().element(selectOne).is().present();
        selectOne.click();
        Graphene.waitAjax().until().element(By.className("ui-selectonemenu-items-wrapper")).is().present();
        String strXpath = "//li[contains(text(),'" + descricao + "')]";
        if (hasListener) {
            Graphene.guardAjax(selectOne.findElement(By.className("ui-selectonemenu-item").xpath(strXpath))).click();
        } else {
            selectOne.findElement(By.className("ui-selectonemenu-item").xpath(strXpath)).click();
        }
    }
}
