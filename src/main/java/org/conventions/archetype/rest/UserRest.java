package org.conventions.archetype.rest;

import com.google.gson.Gson;
import org.conventions.archetype.model.User;
import org.conventions.archetype.security.AppSecurityContext;
import org.conventions.archetype.service.UserService;
import org.conventionsframework.exception.BusinessException;

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
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserRest implements Serializable {

    @Inject
    UserService userService;


    @Inject
    AppSecurityContext appSecurityContext;

    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@Context HttpHeaders headers) {
        List<User> list = userService.getDao().findAll();
        return Response.ok(list).build();
    }

    @GET
    @Path("/find/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") String id) {
        if (id != null) {
            try {
                Long userId = Long.parseLong(id);
                User user = userService.getDao().get(userId);
                return Response.ok(user).build();
            } catch (NumberFormatException nfe) {
                return Response.status(Response.Status.BAD_REQUEST).entity("invalid id to find user:" + id).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("provide user id").build();
        }
    }

    @GET
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeUser(@Context HttpHeaders headers, @PathParam("id") String id) {
        if(headers.getRequestHeaders().containsKey("username") && headers.getRequestHeaders().containsKey("password")){
            User user = userService.findUser(headers.getRequestHeader("username").get(0),headers.getRequestHeader("password").get(0));
            if(user != null)   {
                appSecurityContext.setUser(user);
                appSecurityContext.doLogon();
            }
             else{
                return Response.status(Response.Status.BAD_REQUEST).entity("invalid user credentials").build();
            }
            if (id != null) {
                try {
                    Long userId = Long.parseLong(id);
                    userService.remove(userService.getDao().get(userId));
                    return Response.ok().build();
                } catch (NumberFormatException nfe) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("invalid id to delete user:" + id).build();
                }
                catch (BusinessException be){
                    return Response.status(Response.Status.BAD_REQUEST).entity(be.getMessage()).build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("provide user id").build();
            }
        }else{
            return Response.status(Response.Status.FORBIDDEN).entity("provide username and password").build();

        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Response addUser(@Context HttpHeaders headers, String userJson) {

        if (userJson == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("provide user json").build();
        }
        User user = null;
        try {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("problem at json conversion:" + e.getMessage()).build();
        }
        try {
            userService.store(user);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("problem to persist user" + e.getMessage()).build();
        }

        return Response.ok().entity(user.getId()).build();

    }
}
