package org.conventions.archetype.test.ft;

import org.conventions.archetype.test.ft.pages.HomePage;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 3/9/14.
 */
public class UserFT extends BaseFT {



    @Test
    @InSequence(1)
    public void doLogon(@InitialPage HomePage home){
        assertTrue(home.getLogonDialog().isPresent());
        home.getLogonDialog().doLogon("admin", "admin");
        home.verifyMessage(TestMessageProvider.getMessage("logon.info.successful"));
    }
}
