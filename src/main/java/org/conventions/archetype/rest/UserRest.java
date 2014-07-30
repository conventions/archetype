package org.conventions.archetype.rest;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public interface UserRest {

	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response list();

	@GET
	@Path("/find/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response find(@PathParam("id") String id);

	@GET
	@Path("/findByName/{name}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findByName(@PathParam("name") String name);

	@GET
	@Path("/delete/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response removeUser(@Context HttpHeaders headers, @PathParam("id") String id);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addUser(@Context HttpHeaders headers, String userJson);

}
