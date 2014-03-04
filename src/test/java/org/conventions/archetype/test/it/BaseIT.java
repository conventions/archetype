package org.conventions.archetype.test.it;

import org.conventions.archetype.model.User;
import org.conventions.archetype.security.AppSecurityContext;
import org.conventions.archetype.service.UserService;
import org.conventions.archetype.test.util.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class BaseIT {


    @Inject
    AppSecurityContext appSecurityContext;

    @Inject
    UserService userService;

    @BeforeClass
    public static void initClass() {
        // REST config
        ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(providerFactory);
    }

    @Deployment(testable = true, order = 1)
    public static Archive<?> createDeployment() {
        WebArchive war = Deployments.getBaseDeployment();
        System.out.println(war.toString(true));
        return war;
    }

    protected void login(String username, String password) {
        if (appSecurityContext == null || userService == null) {//in client mode(RunAsClient) will be null
            return;
        }
        User user = userService.findUser(username,password);
        if(user != null)   {
            appSecurityContext.setUser(user);
            appSecurityContext.doLogon();
        }
    }

}
