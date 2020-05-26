package msh.gert.api.dev;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.sql.Connection;
import java.sql.DriverManager;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
public class EndpointTest {
	
	@GET
	public Response ping() {
		Connection conn = null;
		ResponseBuilder response = null;
	
		try {
            Class.forName("org.postgresql.Driver");     
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://postgres/gert", "postgres", "dev_password_1508");
			response = Response.ok("Connection Successfull");
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.ok("OK");
		}
		
		return response.build();
	}
}
