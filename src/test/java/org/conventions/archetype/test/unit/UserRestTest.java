package org.conventions.archetype.test.unit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.conventions.archetype.util.AppConstants;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Assert;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmpestano on 3/1/14.
 */
public class UserRestTest {
    private final String CONTEXT;

    public UserRestTest(String context) {
        this.CONTEXT = context;
    }

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
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/add/");
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

    public void shouldInsertUserWithoutGroup() {
        SimpleUser user = new SimpleUser();
        user.setName("userWithoutGroups");
        user.setPassword("pass");
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/add/");
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

    public void shouldFindUserByName(String name) {
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/findByName/" + name);
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
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


    public void shouldListUsersWithSuccess() {
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/list/");
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String json = response.getEntity(String.class);
            Gson gson = new Gson();
            JsonElement jsonElement = new JsonParser().parse(json);
            Type userListType = new TypeToken<List<SimpleUser>>() {
            }.getType();
            List<SimpleUser> simpleUsers = gson.fromJson(jsonElement, userListType);
            Assert.assertNotNull(simpleUsers);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    public void shouldNotDeleteUserWithGroups() {

        SimpleUser adminUser = findUserByNameViaRest("user rest");
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/delete/" + adminUser.getId());
        //admin username & password to add as headers to check permission(remove method in userService needs authorization to be executed)
        request.header("username", adminUser.getName());
        request.header("password", "pass");//TODO decript adminUser.getPassword()
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            //will fail to remove cause user has groups
            Assert.assertEquals(response.getEntity(), TestMessageProvider.getMessage("be.user.remove"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    public void shouldNotDeleteUserWithNoPermission() {

        SimpleUser nonAdminUser = findUserByNameViaRest("userWithoutGroups");
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/delete/" + nonAdminUser.getId());
        //admin username & password to add as headers to check permission(remove method in userService needs authorization to be executed)
        request.header("username", nonAdminUser.getName());
        request.header("password", "pass");//TODO decript adminUser.getPassword()
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            //will fail to remove cause user has groups
            Assert.assertEquals(response.getEntity(), TestMessageProvider.getMessage("default-security-message"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    public void shouldDeleteUserWithoutGroups(){
        SimpleUser userWithoutGroup = findUserByNameViaRest("userWithoutGroups");
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/delete/" + userWithoutGroup.getId());
        SimpleUser adminUser = findUserByNameViaRest("user rest");
        //admin username & password to add as headers to check permission(remove method in userService needs authorization to be executed)
        request.header("username", adminUser.getName());
        request.header("password", "pass");//TODO decript adminUser.getPassword()        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    private SimpleUser findUserByNameViaRest(String username) {
        Assert.assertNotNull(username);
        SimpleUser user = null;
        try {
            ClientResponse<String> response = new ClientRequest(CONTEXT + "rest/user/findByName/" + username).accept(MediaType.APPLICATION_JSON).get(String.class);
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
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
