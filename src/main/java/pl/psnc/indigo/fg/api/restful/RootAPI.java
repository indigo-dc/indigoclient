package pl.psnc.indigo.fg.api.restful;

import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

public class RootAPI extends BaseAPI {

        private final static Logger LOGGER = Logger.getLogger(RootAPI.class.getName());

	public RootAPI(String httpAddress) {

		super(httpAddress);
	}

	public String getRoot() {

		Client client = ClientBuilder.newClient();
		Response response = client.target(httpAddress)
		  .request(MediaType.TEXT_PLAIN_TYPE)
		  .get();

                String result = null;
                if(response.getStatus() == 200) {
                    result = "status: " 
				+ response.getStatus() 
				+ "\n" 
				+ "headers: " 
				+ response.getHeaders() 
				+ "\n"
				+ "body: "
				+ response.readEntity(String.class);  
                } else {
                    LOGGER.severe("Error while calling: " + httpAddress + " - status: " + response.getStatus());
                    return null;
                }
		return result;	
	}
}
