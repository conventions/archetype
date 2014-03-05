package org.conventions.archetype.test.unit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.conventions.archetype.test.util.TestMessageProvider;
import org.conventions.archetype.util.AppConstants;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmpestano on 3/1/14.
 */
@RunWith(value = JUnit4.class)
public class UserRestTest {
    private String CONTEXT = "http://localhost:8080/archetype/";
    private Long userId;
    private String username;
    private String password;

    public UserRestTest() {
    }

    public void setCONTEXT(String CONTEXT) {
        this.CONTEXT = CONTEXT;
    }

    @BeforeClass
    public static void init() {
        ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(providerFactory);
    }


    @Test
    public void shouldInsertUser(){
        SimpleUser user = new SimpleUser();
        user.setName("name");
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
        group2.setRoles(new ArrayList<SimpleRole>(){{add(new SimpleRole("r3"));}});
        groups.add(group1);
        groups.add(group2);
        user.setGroups(groups);
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/add/");
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try{
            Gson g = new Gson();
            response = request.body(MediaType.APPLICATION_JSON,g.toJson(user)).post(String.class);
            Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
            this.userId = Long.parseLong(response.getEntity(String.class));
            Assert.assertNotNull(userId);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void shouldFindUser(){
        if(userId == null){
            userId = 1L;
        }
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/find/"+userId);
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try{
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
            String json = response.getEntity(String.class);
            Gson gson = new Gson();
            JsonElement jsonElement = new JsonParser().parse(json);
            SimpleUser simpleUser = gson.fromJson(jsonElement, SimpleUser.class);
            Assert.assertNotNull(simpleUser);
        }catch (Exception ex){
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void shouldListUsersWithSuccess(){
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/list/");
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try{
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
            String json = response.getEntity(String.class);
            Gson gson = new Gson();
            JsonElement jsonElement = new JsonParser().parse(json);
            Type userListType = new TypeToken<List<SimpleUser>>() {
            }.getType();
            List<SimpleUser> simpleUsers = gson.fromJson(jsonElement, userListType);
            Assert.assertNotNull(simpleUsers);
        }catch (Exception ex){
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void shouldNotDeleteUserWithGroups(){
        if(userId == null){
            userId = 1L;
        }
        ClientRequest request = new ClientRequest(CONTEXT + "rest/user/delete/"+userId);
        if(username == null){
            username = "name";
        }
        if(password == null){
            password = "pass";
        }
        request.header("username",username);
        request.header("password",password);
        request.accept(MediaType.APPLICATION_JSON);
        ClientResponse<String> response;
        try{
            response = request.get(String.class);
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            Assert.assertEquals(response.getEntity(), TestMessageProvider.getMessage("be.user.remove"));
        }catch (Exception ex){
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCONTEXT() {
        return CONTEXT;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
