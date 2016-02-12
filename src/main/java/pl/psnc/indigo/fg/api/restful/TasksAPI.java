package pl.psnc.indigo.fg.api.restful;

import java.io.File;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientConfig;
import pl.psnc.indigo.fg.api.restful.jaxb.ErrorMessage;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class TasksAPI extends BaseAPI {
    
        private final static Logger LOGGER = Logger.getLogger(TasksAPI.class.getName());
        String tasksHttpAddress = null;

	public TasksAPI(String httpAddress) {
		super(httpAddress);
                tasksHttpAddress = RootAPI.getRootForAddress(httpAddress).getURLAsString() + "tasks";
	}

	/**
	 * Calls create task at server side
         * 
         * To submit task we have to pass Task object filled with description
         * of the task
         * - user
         * - application's id
         * - arguments
         * - description
         * - input files
         * - output files
         * 
         * The set of parameters might be application dependant. For example
         * some applications might require inputs and some other, not.
	 */
	public Task createTask(Task newTask) throws Exception {

                Client client = ClientBuilder.newClient();

                String httpToCall = tasksHttpAddress + "?user=" + newTask.getUser();
                String jsonText = null;
                
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
                    jsonText = mapper.writeValueAsString(newTask);
                } catch(Exception ex) {
                    throw ex;
                }
                
		Entity<String> payload 
			= Entity.json(
				jsonText
				);

                LOGGER.info("Calling: " + httpToCall);
                
		Response response = client.target(httpToCall)
  					.request(MediaType.APPLICATION_JSON_TYPE)
                                        .header("Content-Type","application/json")
                                        .header("Authorization", "Bearer {access_token}")
                                        .accept(MediaType.APPLICATION_JSON_TYPE)
  					.post(payload);
		int status = response.getStatus();
                
                if(status == 200) {
                    LOGGER.info("Response status: " + status);
                    MultivaluedMap<String,Object> headers = response.getHeaders();
                    String body = response.readEntity(String.class);

                    LOGGER.info("Body: " + body);
                    
                    try { 
                            ObjectMapper mapper = new ObjectMapper();
                            Task task = mapper.readValue(body, Task.class);
                            return task;
                    } catch(Exception ex) {
                        LOGGER.log(Level.SEVERE, "Exception while parsing JSON value:", ex);
                        throw ex;
                    } finally {
                      response.close();
                      client.close();  
                    }
                } else {
                    LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                }
                
                response.close();
                client.close();
                
                return null;
	}
	
	/**
         * Submit task
	 * 
         */
        public Upload uploadFileForTask(Task task, String inputURL, File file) throws Exception {

                Client client = ClientBuilder.newClient();

                String httpToCall = tasksHttpAddress + "/" + task.getId() + "/input?user=" + task.getUser();

                LOGGER.info("Calling: " + httpToCall);
                
                Response response = uploadFile(httpToCall, file);
                
                int status = response.getStatus();
		
                LOGGER.info("Status:" + status);
                
                if(status == 200) {
                    MultivaluedMap<String,Object> headers = response.getHeaders();
                    String body = response.readEntity(String.class);

                    LOGGER.info("Body: " + body);
                    
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Upload upload = mapper.readValue(body, Upload.class);
                        return upload;
                    } catch(Exception ex) {
                        LOGGER.log(Level.SEVERE, "Exception while parsing JSON value:", ex);
                        throw ex;
                    }
                } else {
                    LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                    return null;
                }
        }

	/**
         * Check status
         * 
         */
        public Task getTask(Task task) throws Exception {

                Client client = ClientBuilder.newClient();

                String httpToCall = tasksHttpAddress + "/" + task.getId();

                LOGGER.info("Calling: " + httpToCall);

                Entity<String> payload
                        = Entity.text( "" );

                Response response = client.target(httpToCall)
                                        .request(MediaType.APPLICATION_JSON_TYPE)
                                        .header("Content-Type","application/json")
                                        .header("Authorization", "Bearer {access_token}")
                                        .accept(MediaType.APPLICATION_JSON_TYPE)
                                        .get(Response.class);
                int status = response.getStatus();
                
                LOGGER.info("Status:" + status);
                
                MultivaluedMap<String,Object> headers = response.getHeaders();
                String body = response.readEntity(String.class);

                LOGGER.info("Body: " + body);
                
                if(status == 200) {

                    try {
                            ObjectMapper mapper = new ObjectMapper();
                            Task retVal = mapper.readValue(body, Task.class);
                            return retVal;
                    } catch(Exception ex) {
                        LOGGER.log(Level.SEVERE, "Exception while parsing JSON value:", ex);
                        return null;
                    }
                } else {
                  LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                  return null;  
                }
        }
        
        public Task[] getAllTasks() throws Exception {
                Client client = ClientBuilder.newClient();

                String httpToCall = tasksHttpAddress;

                LOGGER.info("Calling: " + httpToCall);

                Entity<String> payload
                        = Entity.text( "" );

                Response response = client.target(httpToCall)
                                        .request(MediaType.APPLICATION_JSON_TYPE)
                                        .header("Content-Type","application/json")
                                        .header("Authorization", "Bearer {access_token}")
                                        .accept(MediaType.APPLICATION_JSON_TYPE)
                                        .get(Response.class);
                int status = response.getStatus();
                LOGGER.info("Status:" + status);
                MultivaluedMap<String,Object> headers = response.getHeaders();
                String body = response.readEntity(String.class);

                LOGGER.info("Body: " + body);

                if(status == 200) {
                    try {
                            ObjectMapper mapper = new ObjectMapper();
                            Task [] tasks = mapper.readValue(body, Task[].class);
                            return tasks;
                    } catch(Exception ex) {
                        LOGGER.log(Level.SEVERE, "Exception while parsing JSON value:", ex);
                        throw ex;
                    }
                } else {
                    LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                    ObjectMapper mapper = new ObjectMapper();
                    ErrorMessage message = mapper.readValue(body, ErrorMessage.class);
                    throw new Exception("Error message: " + message.getMessage());
                }
        }
        
        /**
         * Uploads file into given URL
         * 
         * @param httpURL - URL that accepts file input
         * @param filePath - location of input file
         * @return
         * @throws Exception 
         */
        private static Response uploadFile(String httpURL, File filePath)  throws Exception {
 
            // local variables
            ClientConfig clientConfig = null;
            Client client = null;
            WebTarget webTarget = null;
            Invocation.Builder invocationBuilder = null;
            Response response = null;
            FileDataBodyPart fileDataBodyPart = null;
            FormDataMultiPart formDataMultiPart = null;
            int responseCode;
            String responseMessageFromServer = null;
            String responseString = null;

            try{
                // invoke service after setting necessary parameters
                clientConfig = new ClientConfig();
                clientConfig.register(MultiPartFeature.class);
                client =  ClientBuilder.newClient(clientConfig);
                webTarget = client.target(httpURL);

                // set file upload values
                fileDataBodyPart = new FileDataBodyPart(filePath.getName(), filePath, MediaType.APPLICATION_OCTET_STREAM_TYPE);
                formDataMultiPart = new FormDataMultiPart();
                formDataMultiPart.bodyPart(fileDataBodyPart);

                // invoke service
                invocationBuilder = webTarget.request();
                //          invocationBuilder.header("Authorization", "Basic " + authorization);
                response = invocationBuilder.post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));

                // get response code
                responseCode = response.getStatus();
                LOGGER.info("Response code: " + responseCode);

                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed with HTTP error code : " + responseCode);
                }

                return response;
                
            }
            catch(Exception ex) {
                LOGGER.log(Level.SEVERE, "Error while uploading file to HTTP address", ex);
                throw new RuntimeException("Failed to perform HTTP call");
            }
//            finally{
//                // release resources, if any
//                fileDataBodyPart.cleanup();
//                formDataMultiPart.cleanup();
//                formDataMultiPart.close();
//                response.close();
//                client.close();
//            }
//            return responseString;
        }
}	
