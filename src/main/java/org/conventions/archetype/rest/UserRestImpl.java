package org.conventions.archetype.rest;

import com.google.gson.Gson;
import org.conventions.archetype.model.User;
import org.conventions.archetype.security.SecurityContextImpl;
import org.conventions.archetype.service.UserService;
import org.conventionsframework.crud.Crud;
import org.conventionsframework.exception.BusinessException;
import org.hibernate.criterion.MatchMode;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

/**
 * Created by rmpestano on 3/1/14.
 */

public class UserRestImpl implements Serializable, UserRest {

    @Inject
    UserService userService;

    @Inject
    Crud<User> userCrud;


    @Inject
    SecurityContextImpl appSecurityContext;

     
    public Response list() {
        List<User> list = userService.crud().listAll();
        return Response.ok(list).build();
    }

   
    public Response find(String id) {
        if (id != null) {
            try {
                Long userId = Long.parseLong(id);
                User user = userService.crud().get(userId);
                return Response.ok(user).build();
            } catch (NumberFormatException nfe) {
                return Response.status(Response.Status.BAD_REQUEST).entity("invalid id to find user:" + id).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("provide user id").build();
        }
    }

    public Response findByName(String name) {
        if (name != null) {
            User userQuery = new User();
            userQuery.setName(name);
            User user = userService.crud().example(userQuery,MatchMode.EXACT).find();
            if (user == null) {
                return Response.status(Response.Status.NO_CONTENT).entity("user not found with name:" + name).build();
            }
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("provide user name").build();
        }
    }

    
    public Response removeUser(HttpHeaders headers, String id) {
        if (headers.getRequestHeaders().containsKey("username") && headers.getRequestHeaders().containsKey("password")) {
            String username = headers.getRequestHeader("username").get(0);
            String password = headers.getRequestHeader("password").get(0);
            try {
                appSecurityContext.doLogon(username, password);
            } catch (BusinessException be) {
                return Response.status(Response.Status.BAD_REQUEST).entity(be.getMessage()).build();
            }
            if (id != null) {
                try {
                    Long userId = Long.parseLong(id);
                    User u = new User();
                    u.setId(userId);
                    userService.remove(userService.crud().load(u.getId()));
                    return Response.ok().build();
                } catch (NumberFormatException nfe) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("invalid id to delete user:" + id).build();
                } catch (BusinessException be) {
                    if (be.getSeverity() == null) {
                        return Response.status(Response.Status.BAD_REQUEST).entity(be.getMessage()).build();
                    } else {
                        return Response.status(Response.Status.UNAUTHORIZED).entity(be.getMessage()).build();
                    }
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("provide user id").build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("provide username and password").build();

        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Response addUser(HttpHeaders headers, String userJson) {

        if (userJson == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("provide user json").build();
        }
        User user = null;
        try {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("problem at json conversion:" + e.getMessage()).build();
        }
        try {
            userService.store(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("problem to persist user" + e.getMessage()).build();
        }

        return Response.ok().entity(user.getId()).build();

    }
}
