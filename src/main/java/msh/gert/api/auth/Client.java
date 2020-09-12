package msh.gert.api.auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msh.gert.core.auth.AuthorizationService;

@Path("client")
public class Client {
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerClient(@FormParam("name") String name, @FormParam("owner_id") int ownerId, @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        AuthorizationService authorizationService = new AuthorizationService();
        authorizationService.basicAuth(authorizationHeader);

        return Response.ok("{ 'auth': '"+authorizationHeader+"'}").build();
    }

    @GET
    public Response getClient() {
        return Response.status(Response.Status.ACCEPTED).build();
    }
}