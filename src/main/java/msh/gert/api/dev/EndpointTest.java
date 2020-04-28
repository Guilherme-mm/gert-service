package msh.gert.api.dev;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
public class EndpointTest {
	
	@GET
	public Response ping() {
		return Response.ok("I'm alive").build();
	}
}
