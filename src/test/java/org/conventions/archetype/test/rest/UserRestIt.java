package org.conventions.archetype.test.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.conventions.archetype.rest.UserRest;
import org.conventions.archetype.rest.UserRestImpl;
import org.conventions.archetype.test.util.Deployments;
import org.conventions.archetype.util.AppConstants;
import org.conventionsframework.util.ResourceBundle;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by rmpestano on 3/1/14.
 */
@RunWith(Arquillian.class)
public class UserRestIt {

    @ArquillianResource
    protected URL context;

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        WebArchive war = Deployments.getBaseDeployment().
        addClass(RestDataset.class);
        System.out.println(war.toString(true));
        return war;
    }

    private ResourceBundle resourceBundle;

    public UserRestIt() {
        try {
            resourceBundle = new ResourceBundle("messages",Locale.ENGLISH);//force test locale, see test-faces-config
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    @InSequence(1)
    public void shouldListUsersWithSuccess() {
        ClientRequest request = new ClientRequest(context + "rest/user/list/");
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String json = response.getEntity(String.class);
            Gson gson = new Gson();
            JsonElement jsonElement = new JsonParser().parse(json);
            Type userListType = new TypeToken<List<SimpleUser>>() {
            }.getType();
            List<SimpleUser> simpleUsers = gson.fromJson(jsonElement, userListType);
            Assert.assertNotNull(simpleUsers);
            assertEquals(simpleUsers.size(),2);//rest dataset has 2 users @see RestDataset.java
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }
    
    
    //using rest extensions
    @Test
    @InSequence(1)
    public void shouldListUsers(@ArquillianResteasyResource UserRest userRest){
    	 ClientResponse<String> response = (ClientResponse<String>) userRest.list();
    	 Assert.assertNotNull(response);
         assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
         String json = response.getEntity(String.class);
         Gson gson = new Gson();
         JsonElement jsonElement = new JsonParser().parse(json);
         Type userListType = new TypeToken<List<SimpleUser>>() {
         }.getType();
         List<SimpleUser> simpleUsers = gson.fromJson(jsonElement, userListType);
         Assert.assertNotNull(simpleUsers);
         assertEquals(simpleUsers.size(),2);//rest dataset has 2 users @see RestDataset.java
    }

    @Test
    @InSequence(2)
    public void shouldInsertUserWithGroups() {
        SimpleUser user = new SimpleUser();
        user.setName("user rest");
        user.setPassword("pass");
        List<SimpleGroup> groups = new ArrayList<SimpleGroup>();
        List<SimpleRole> roles = new ArrayList<SimpleRole>();
        SimpleRole r1 = new SimpleRole();
        r1.setName(AppConstants.Role.ADMIN);
        SimpleRole r2 = new SimpleRole();
        r2.setName("r2");
        roles.add(r1);
        roles.add(r2);
        SimpleGroup group1 = new SimpleGroup();
        group1.setName("group1");
        group1.setRoles(roles);
        SimpleGroup group2 = new SimpleGroup();
        group2.setName("group2");
        group2.setRoles(new ArrayList<SimpleRole>() {{
            add(new SimpleRole("r3"));
        }});
        groups.add(group1);
        groups.add(group2);
        user.setGroups(groups);
        ClientRequest request = new ClientRequest(context + "rest/user/add/");
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response = null;
        try {
            Gson g = new Gson();
            response = request.body(MediaType.APPLICATION_JSON, g.toJson(user)).post(String.class);
            Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
            Long userId = Long.parseLong(response.getEntity(String.class));
            Assert.assertNotNull(userId);
        } catch (Exception e) {
            e.printStackTrace();
            String error = e.getMessage();
            if(response != null){
                error += response.getEntity();
            }
            Assert.fail(error);
        }
    }
    
    @Test
    @InSequence(3)
    public void shouldInsertUserWithoutGroup() {
        SimpleUser user = new SimpleUser();
        user.setName("userWithoutGroups");
        user.setPassword("pass");
        ClientRequest request = new ClientRequest(context + "rest/user/add/");
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            Gson g = new Gson();
            response = request.body(MediaType.APPLICATION_JSON, g.toJson(user)).post(String.class);
            Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
            Long userId = Long.parseLong(response.getEntity(String.class));
            Assert.assertNotNull(userId);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    @InSequence(4)
    public void shouldFindUserByName() {
        ClientRequest request = new ClientRequest(context + "rest/user/findByName/restUser");//restDataset has UserRest
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String json = response.getEntity(String.class);
            Gson gson = new Gson();
            JsonElement jsonElement = new JsonParser().parse(json);
            SimpleUser simpleUser = gson.fromJson(jsonElement, SimpleUser.class);
            Assert.assertNotNull(simpleUser);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }



    @Test
    @InSequence(5)
    public void shouldNotDeleteUserWithGroups() {
        SimpleUser adminUser = findUserByNameViaRest("restUser");
        ClientRequest request = new ClientRequest(context + "rest/user/delete/" + adminUser.getId());
        //admin username & password to add as headers to check permission(remove method in userService needs authorization to be executed)
        request.header("username", adminUser.getName());
        request.header("password", "pass");//TODO decript adminUser.getPassword()
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            //will fail to remove cause user has groups
            assertEquals(response.getEntity(), resourceBundle.getString("be.user.remove"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    @InSequence(6)
    public void shouldNotDeleteUserWithNoPermission() {
        SimpleUser nonAdminUser = findUserByNameViaRest("userWithoutGroups");
        ClientRequest request = new ClientRequest(context + "rest/user/delete/" + nonAdminUser.getId());
        //admin username & password to add as headers to check permission(remove method in userService needs authorization to be executed)
        request.header("username", nonAdminUser.getName());
        request.header("password", "pass");//TODO decript adminUser.getPassword()
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            //will fail to remove cause user has groups
            assertEquals(response.getEntity(), resourceBundle.getString("default-security-message"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    @InSequence(7)
    public void shouldDeleteUserWithoutGroups(){
        SimpleUser userWithoutGroup = findUserByNameViaRest("userWithoutGroups");
        ClientRequest request = new ClientRequest(context + "rest/user/delete/" + userWithoutGroup.getId());
        SimpleUser adminUser = findUserByNameViaRest("user rest");
        //admin username & password to add as headers to check permission(remove method in userService needs authorization to be executed)
        request.header("username", adminUser.getName());
        request.header("password", "pass");//TODO decript adminUser.getPassword()        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }
    

    private SimpleUser findUserByNameViaRest(String username) {
        Assert.assertNotNull(username);
        SimpleUser user = null;
        try {
            ClientResponse<String> response = new ClientRequest(context + "rest/user/findByName/" + username).accept(MediaType.APPLICATION_JSON).get(String.class);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String json = response.getEntity(String.class);
            Assert.assertNotNull(json);
            JsonElement jsonElement = new JsonParser().parse(json);
            Gson gson = new Gson();
            user = gson.fromJson(jsonElement, SimpleUser.class);
            Assert.assertNotNull(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


}
