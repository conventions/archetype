package org.conventions.archetype.test.warp;

import org.conventions.archetype.test.ft.pages.HomePage;
import org.conventionsframework.security.SecurityContext;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.client.filter.http.HttpMethod;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.junit.Test;

import javax.inject.Inject;
import java.io.Serializable;

import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by rmpestano on 4/11/14.
 */
public class LogonWarp extends BaseWarp implements Serializable {
    


        @Test
        @InSequence(1)
        public void shouldLogonWithSuccess(@InitialPage final HomePage home){
            assertTrue(home.getLogonDialog().isPresent());
            Warp.initiate(new Activity() {


                @Override
                public void perform() {
                    home.getLogonDialog().doLogon("admin", "admin");

                }
            })
                    .observe(request().method().equal(HttpMethod.POST).uri().contains("home.faces"))
                    .inspect(new Inspection() {
                        private static final long serialVersionUID = 1L;

                        @Inject
                        SecurityContext securityContext;

                        @BeforePhase(Phase.RENDER_RESPONSE)
                        public void shouldNotBeLoggedIn() {
                            System.out.println("warp loggerin:" + securityContext.loggedIn());
                            assertFalse(securityContext.loggedIn());
                        }

                        @AfterPhase(Phase.INVOKE_APPLICATION)
                        public void shouldBeLoggedIn() {
                            System.out.println("warp loggedin:" + securityContext.loggedIn());
                            assertTrue(securityContext.loggedIn());
                        }

                    });


            //"logon.info.successful"
        }
        

}
