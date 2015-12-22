package pl.psnc.indigo.fg.api.restful;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;

public class TasksAPI extends BaseAPI {

	public TasksAPI(String httpAddress) {
		super(httpAddress);
                this.httpAddress = httpAddress + "/v1.0/tasks?user=";
	}

	/**
	 * Calls prepare task at server side
	 */
	public Task prepareTask(String user, String application, String description) {
//		ClientConfig cc = new ClientConfig().register(new JacksonFeature());
//              Client client = ClientBuilder.newClient(cc);

                Client client = ClientBuilder.newClient();

                String httpToCall = httpAddress + user;
                
		Entity<String> payload 
			= Entity.json(
				" {  \"application\": \"" + application + "\" ,  \"description\": \"" + description + "\" }"
				);

		System.out.println("Calling: " + httpToCall);
                
		Response response = client.target(httpToCall)
  					.request(MediaType.APPLICATION_JSON_TYPE)
                                        .header("Content-Type","application/json")
                                        .header("Authorization", "Bearer {access_token}")
                                        .accept(MediaType.APPLICATION_JSON_TYPE)
  					.post(payload);
		int status = response.getStatus();
		System.out.println("Status: " + status);
		MultivaluedMap<String,Object> headers = response.getHeaders();
	        String body = response.readEntity(String.class);

		System.out.println(body);

                
                try { 
//			JSONObject jsonOutput = new JSONObject( body ); 
                        ObjectMapper mapper = new ObjectMapper();
                        Task task = mapper.readValue(body, Task.class);
                        return task;
		} catch(Exception ex) {
			return null;
		}
	}
}	
