package org.conventions.archetype.test.bdd.user;

import org.conventions.archetype.test.bdd.BaseBdd;
import org.conventions.archetype.test.bdd.Steps;
import org.conventions.archetype.test.util.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: rmpestano
 * Date: 10/31/13
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(Arquillian.class)
@Steps(UserSteps.class)
public class UserBdd extends BaseBdd {


    @Deployment
    public static WebArchive createDeployment()
    {
        WebArchive archive = Deployments.getBaseDeployment()
                .addPackage(BaseBdd.class.getPackage())
                .addClass(UserSteps.class);
        return archive;
    }

}
