package control;

import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import sonarAPI.model.Views;

public class RestHandler 
{
	private static final String uri = "http://ttp10-snar.ca-technologies.fr/api/views/list";
	
	/**
	 * @param arg
	 */
	public static void main (String... arg) 
	{
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget  = client.target("http://ttp10-snar.ca-technologies.fr");
		WebTarget listViews = webTarget.path("api/views/list");
		String name = "ETP8137";
        String password = "28H02m89,;:!";
        
        String authString = name + ":" + password;
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
        System.out.println("Base64 encoded auth string: " + authStringEnc);
		Invocation.Builder invocationBuilder = listViews.request(MediaType.APPLICATION_JSON).header("Authorization", authStringEnc) ;
		Views response = invocationBuilder.get(Views.class);

  
	}
	
	
}
