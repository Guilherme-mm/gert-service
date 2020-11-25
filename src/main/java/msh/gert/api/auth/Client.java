package msh.gert.api.auth;

import java.util.HashMap;

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

import com.google.gson.Gson;

import msh.gert.control.auth.ClientController;
import msh.gert.core.auth.AuthorizationService;
import msh.gert.db.exceptions.DBException;

@Path("client")
public class Client {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerClient(@FormParam("name") String name, @FormParam("owner_id") int ownerId, @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        //Trying to authenticate the client
        try {
            AuthorizationService authorizationService = new AuthorizationService();
            boolean authenticationSuccessfull = authorizationService.basicAuth(authorizationHeader);

            if(!authenticationSuccessfull){
                //blocks the client
                return Response.status(403).build();
            }
        } catch(DBException error){
            //error treatment
            return Response.serverError().build();
        }

        //Trying to create the client with provided data
        try {
            //Requests the creation of a new client for the resource owner
            ClientController controller = new ClientController();
            boolean clientCreationSuccessfull = true;
            HashMap<String, String> clientCredentials = controller.createClient(name, ownerId);
            
            Gson gson = new Gson();
            String json = gson.toJson(clientCredentials); 
            //Sends a success response
            return Response.ok(json).build();
        } catch (DBException e) {
            String errorMessage = e.getMessage();
            return Response.serverError().build();
        }
    }

    @GET
    public Response getClient() {
        return Response.status(Response.Status.ACCEPTED).build();
    }
}