package org.conventions.archetype.test.it;

import org.conventions.archetype.security.SecurityContextImpl;
import org.conventions.archetype.service.UserService;
import org.conventions.archetype.test.it.role.RoleIt;
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
public class BaseIt {

    @Inject
    protected SecurityContextImpl securityContext;

    @Inject
    protected UserService userService;

    @BeforeClass
    public static void initClass() {
        // REST config
        ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(providerFactory);
    }

    @Deployment(testable = true, order = 1)
    public static Archive<?> createDeployment() {
        WebArchive war = Deployments.getBaseDeployment().
        addClass(RoleIt.class).
        addClass(BaseIt.class).
        addClass(ArchetypeIt.class);


        System.out.println(war.toString(true));
        return war;
    }


    protected void login(String username, String password) {
        if (securityContext == null) {//in client mode(RunAsClient) will be null
            return;
        }
        securityContext.setUserService(userService);//not allowed multiple extended persistenceContexts
        //they must use same persistence context cause userService has extended persistence context
        securityContext.doLogon(username,password);
    }

}
