package org.conventions.archetype.test.it;

import org.conventions.archetype.service.RoleService;
import org.conventions.archetype.service.RoleServiceImpl;
import org.conventions.archetype.service.UserService;
import org.conventions.archetype.service.UserServiceImpl;
import org.conventions.archetype.test.util.TestService;
import org.conventionsframework.qualifier.Log;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Created by rmpestano on 4/19/14.
 */
@RunWith(Arquillian.class)
public class HelloArquillianIt {

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
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.conventionsframework:conventions-core:1.0.0-RC2").withTransitivity().asFile());//conventions
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces:4.0").withoutTransitivity().asSingleFile());

        //WEB-INF
        war.addAsWebInfResource("test-beans.xml", "beans.xml");
        war.addAsWebInfResource("test-web.xml", "web.xml");
        war.addAsWebInfResource("test-faces-config.xml", "faces-config.xml");

        war.addAsWebInfResource("jbossas-ds.xml", "jbossas-ds.xml");//datasource

        //resources
        war.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        war.addAsResource("test-messages_pt.properties", "messages_pt.properties");//test resource bundle
        war.addAsResource("test-messages_en.properties", "messages_en.properties");

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
        int numRoles = roleService.getDao().countAll();
        log.info("COUNT:"+numRoles);
        assertEquals(numRoles, 2);
    }
}
