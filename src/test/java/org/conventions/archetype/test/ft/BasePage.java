package org.conventions.archetype.test.ft;

import junit.framework.Assert;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;

import java.io.Serializable;


public abstract class BasePage implements Serializable {


    public String getLocation() {
        if (!getClass().isAnnotationPresent(Location.class)) {
            throw new RuntimeException("Provide @Location annotation for class:" + getClass().getSimpleName() + " informando sua localização");
        }
        Location location = getClass().getAnnotation(Location.class);
        return location.value();
    }

    @FindByJQuery("div.ui-growl-message")
    private GrapheneElement growl;

    public void verifyMessage(String msg) {
        Graphene.waitAjax().until().element(growl).is().present();
        Assert.assertEquals(msg.trim(), growl.getText().trim());
    }

}
