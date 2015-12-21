package pl.psnc.indigo.fg.api.restful;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

public class RootAPI extends BaseAPI {


	public RootAPI(String httpAddress) {

		super(httpAddress);
	}

	public String getRoot() {

		Client client = ClientBuilder.newClient();
		Response response = client.target(httpAddress)
		  .request(MediaType.TEXT_PLAIN_TYPE)
		  .get();

		String result = "status: " 
				+ response.getStatus() 
				+ "\n" 
				+ "headers: " 
				+ response.getHeaders() 
				+ "\n"
				+ "body: "
				+ response.readEntity(String.class);  
		return result;	

	}

}
