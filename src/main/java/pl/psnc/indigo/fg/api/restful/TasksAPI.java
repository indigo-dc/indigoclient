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
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

public class TasksAPI extends BaseAPI {

	public TasksAPI(String httpAddress) {
		super(httpAddress);
		// TODO: make sure we can get this information directly from the ROOT
		// if ROOT have up to date information, we should initialize client
		// with the call to ROOT
                this.httpAddress = httpAddress + "/v1.0/tasks";
	}

	/**
	 * Calls prepare task at server side
	 */
	public Task prepareTask(String user, String application, String description) throws Exception {


                Client client = ClientBuilder.newClient();

                String httpToCall = httpAddress + "?user=" + user;
                
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
                        ObjectMapper mapper = new ObjectMapper();
                        Task task = mapper.readValue(body, Task.class);
                        return task;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	/**
         * Submit task
	 * TODO: replace arguments with Task object, serialize object to JSON and pass it to WS
         */
        public Upload submitTask(String user, String id) throws Exception {

                Client client = ClientBuilder.newClient();

                String httpToCall = httpAddress + "/" + id + "/input?user=" + user;

                System.out.println("Calling: " + httpToCall);

		Entity<String> payload
                        = Entity.text( "" );

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
                        ObjectMapper mapper = new ObjectMapper();
                        Upload upload = mapper.readValue(body, Upload.class);
                        return upload;
                } catch(Exception ex) {
                        throw ex;
                }
        }

	/**
         * Check status
         * TODO: replace arguments with Task object, serialize object to JSON and pass it to WS
         */
        public Task getTask(String user, String id) throws Exception {

                Client client = ClientBuilder.newClient();

                String httpToCall = httpAddress + "/" + id;

                System.out.println("Calling: " + httpToCall);

                Entity<String> payload
                        = Entity.text( "" );

                Response response = client.target(httpToCall)
                                        .request(MediaType.APPLICATION_JSON_TYPE)
                                        .header("Content-Type","application/json")
                                        .header("Authorization", "Bearer {access_token}")
                                        .accept(MediaType.APPLICATION_JSON_TYPE)
                                        .get(Response.class);
                int status = response.getStatus();
                System.out.println("Status: " + status);
                MultivaluedMap<String,Object> headers = response.getHeaders();
                String body = response.readEntity(String.class);

                System.out.println(body);


                try {
                        ObjectMapper mapper = new ObjectMapper();
                        Task task = mapper.readValue(body, Task.class);
                        return task;
                } catch(Exception ex) {
                        throw ex;
                }
        }
        
        public Task[] getAllTasks() throws Exception {
                Client client = ClientBuilder.newClient();

                String httpToCall = httpAddress;

                System.out.println("Calling: " + httpToCall);

                Entity<String> payload
                        = Entity.text( "" );

                Response response = client.target(httpToCall)
                                        .request(MediaType.APPLICATION_JSON_TYPE)
                                        .header("Content-Type","application/json")
                                        .header("Authorization", "Bearer {access_token}")
                                        .accept(MediaType.APPLICATION_JSON_TYPE)
                                        .get(Response.class);
                int status = response.getStatus();
                System.out.println("Status: " + status);
                MultivaluedMap<String,Object> headers = response.getHeaders();
                String body = response.readEntity(String.class);

                System.out.println(body);


                try {
                        ObjectMapper mapper = new ObjectMapper();
                        Task [] tasks = mapper.readValue(body, Task[].class);
                        return tasks;
                } catch(Exception ex) {
                        throw ex;
                }
        }
}	
