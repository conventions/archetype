package org.conventions.archetype.test.warp;

import org.conventions.archetype.rest.UserRest;
import org.conventions.archetype.test.rest.RestDataset;
import org.conventions.archetype.test.rest.SimpleUser;
import org.conventions.archetype.test.util.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.warp.api.HttpMethod;
import org.jboss.arquillian.extension.rest.warp.api.RestContext;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ws.rs.core.Response;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by rmpestano on 7/29/14.
 * 
 * NOT WORKING YET
 */
@RunWith(Arquillian.class)
@WarpTest
public class RestWarp {


    @ArquillianResource
    protected URL context;

    protected UserRest userRest;


    @Deployment(testable = true)
    @OverProtocol("Servlet 3.0")
    public static Archive<?> createDeployment() {
        WebArchive war = Deployments.getBaseDeployment()
                .addClass(RestDataset.class);
        System.out.println(war.toString(true));
        return war;
    }


    @BeforeClass
    public static void setUpClass() {
        // initializes the rest easy client framework
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    }

    
    @Before
    public void setUp() {
    	userRest = ProxyFactory.create(UserRest.class, context + "rest");
    }


    @Test
    @RunAsClient
    public void shouldFindUser() {
        // creates the stock
    	 Warp.initiate(new Activity() {
             @Override
             public void perform() {
                 ClientResponse<String> response = (ClientResponse<String>) userRest.findByName("restUser");
                 Assert.assertNotNull(response);
                 assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
             }
         }).inspect(new Inspection() {

             private static final long serialVersionUID = 1L;

             @ArquillianResource
             private RestContext restContext;

             @AfterServlet
             public void testGetStock() {
            	 String json = (String) restContext.getHttpResponse().getEntity();
                 Gson gson = new Gson();
                 JsonElement jsonElement = new JsonParser().parse(json);
                 SimpleUser simpleUser = gson.fromJson(jsonElement, SimpleUser.class);
                 assertNotNull(simpleUser);
                 assertEquals("restUser", simpleUser.getName());
                 assertEquals(HttpMethod.GET, restContext.getHttpRequest().getMethod());
                 assertEquals(200, restContext.getHttpResponse().getStatusCode());
                 assertEquals("application/json", restContext.getHttpResponse().getContentType());
                 assertNotNull(restContext.getHttpResponse().getEntity());
             }
          });
     }

}
