package org.conventions.archetype.test.util;

import org.conventions.archetype.bean.ComboMBean;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

import java.io.File;

/**
 * @author rafael-pestano 20/07/2013 16:49:34
 *         <p/>
 *         Arquillian WebArchive factory
 */
public class Deployments {


    protected static final String WEBAPP_SRC = "src/main/webapp";
    protected static final String WEB_INF= "src/main/webapp/WEB-INF";

    /**
     * @return base WebArchive for all arquillian tests
     */
    public static WebArchive getBaseDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class);
        war.addPackages(true, "org.conventions.archetype.model");
        war.addPackages(true, "org.conventions.archetype.rest");
        war.addPackages(true, "org.conventions.archetype.service");
        war.addPackages(true, "org.conventions.archetype.security");
        war.addPackages(true, "org.conventions.archetype.event");
        war.addPackages(true, "org.conventions.archetype.configuration");
        war.addPackages(true, "org.conventions.archetype.qualifier");
        war.addPackages(true, "org.conventions.archetype.util").
        addClass(ComboMBean.class).addClass(TestService.class);
        //LIBS
        MavenResolverSystem resolver = Maven.resolver();
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.conventionsframework:conventions-core:1.0.0").withoutTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.httpcomponents:httpcore:4.2.5").withTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces:4.0").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces.extensions:all-themes:1.0.8").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.omnifaces:omnifaces:1.6").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.codehaus.jackson:jackson-core-asl:1.5.5").withTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces.extensions:primefaces-extensions:1.0.0").withTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.myfaces.extensions.cdi.bundles:myfaces-extcdi-bundle-jsf20:1.0.6").withoutTransitivity().asSingleFile());
        //WEB-INF

        war.addAsWebInfResource(new File(WEB_INF,"beans.xml"), "beans.xml");
        war.addAsWebInfResource(new File(WEB_INF,"web.xml"), "web.xml");
        war.addAsWebInfResource(new File(WEB_INF,"faces-config.xml"), "faces-config.xml");
        //war.addAsWebInfResource("test-jboss-deployment-structure.xml", "jboss-deployment-structure.xml");

        war.addAsWebInfResource("jbossas-ds.xml", "jbossas-ds.xml");

        //resources
        war.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        war.addAsResource("messages_pt.properties", "messages_pt.properties");
        war.addAsResource("messages_en.properties", "messages_en.properties");

        return war;
    }

}
