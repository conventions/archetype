package org.conventions.archetype.test.it;

import org.conventions.archetype.service.RoleService;
import org.conventions.archetype.service.RoleServiceImpl;
import org.conventions.archetype.service.UserService;
import org.conventions.archetype.service.UserServiceImpl;
import org.conventions.archetype.test.util.TestService;
import org.conventionsframework.qualifier.Log;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.io.File;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Created by rmpestano on 4/19/14.
 */
@RunWith(Arquillian.class)
public class HelloArquillianIt {

    private static final String WEB_INF= "src/main/webapp/WEB-INF";

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class);
        war.addPackages(true, "org.conventions.archetype.model");//entities
        war.addClasses(RoleService.class, RoleServiceImpl.class) //we will test RoleService
        .addClasses(UserService.class, UserServiceImpl.class)//used by SecurityInterceptorImpl.java
        .addPackages(true, "org.conventions.archetype.qualifier")//@ListToUpdate, @see roleServiceImpl
        .addClass(TestService.class);
        war.addPackages(true, "org.conventions.archetype.security");//security interceptor @see beans.xml
        war.addPackages(true, "org.conventions.archetype.event");//UpdateListEvent @seeGroupServiceImpl#afterStore
        war.addPackages(true, "org.conventions.archetype.util");
        //LIBS
        MavenResolverSystem resolver = Maven.resolver();
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.conventionsframework:conventions-core:1.3.1").withTransitivity().asFile());//conventions
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces:4.0").withoutTransitivity().asSingleFile());

        //WEB-INF
        war.addAsWebInfResource(new File(WEB_INF,"beans.xml"), "beans.xml");
        war.addAsWebInfResource(new File(WEB_INF,"web.xml"), "web.xml");
        war.addAsWebInfResource(new File(WEB_INF,"faces-config.xml"), "faces-config.xml");
        war.addAsResource("messages_en.properties", "messages_en.properties");
        war.addAsWebInfResource("jbossas-ds.xml", "jbossas-ds.xml");//datasource

        //resources
        war.addAsResource("test-persistence.xml", "META-INF/persistence.xml");

        return war;
    }

    @Inject
    RoleService roleService;

    @Inject
    TestService testService;

    @Inject
    @Log
    transient Logger log;

    @Test
    public void shouldListRolesWithSuccess(){
        testService.clearDatabase();
        testService.createRoleDataset();
        int numRoles = roleService.crud().countAll();
        log.info("COUNT:"+numRoles);
        assertEquals(numRoles, 2);
    }

    @Test
    @UsingDataSet("role.yml")
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    public void shouldListRolesUsingDataset(){
        int numRoles = roleService.crud().countAll();
        log.info("COUNT:"+numRoles);
        assertEquals(numRoles, 3);
    }
}
