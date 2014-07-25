package org.conventions.archetype.test.bdd.user;

import org.conventions.archetype.test.bdd.BaseBdd;
import org.conventions.archetype.test.bdd.Steps;
import org.conventions.archetype.test.util.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
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
                .addAsResource("org/conventions/archetype/test/bdd/user/user_bdd.story")
                .addClass(UserSteps.class);
        
        MavenResolverSystem resolver = Maven.resolver();
        archive.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.google.guava:guava:11.0.1").withTransitivity().asFile());
        archive.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.jbehave:jbehave-core:3.7.5").withTransitivity().asFile());
        return archive;
    }

}
