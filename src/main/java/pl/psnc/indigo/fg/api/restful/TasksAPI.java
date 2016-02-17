package pl.psnc.indigo.fg.api.restful;

import java.io.File;
import java.io.IOException;
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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import pl.psnc.indigo.fg.api.restful.jaxb.ErrorMessage;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
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

                String httpToCall = tasksHttpAddress + "?user=" + newTask.getUser();
                LOGGER.info("Calling: " + httpToCall);
            
                Client      client = null;
                Response    response = null;
                
                try {
                    client = ClientBuilder.newClient();
                    
                    String jsonText = null;

                    ObjectMapper inputMapper = new ObjectMapper();
                    inputMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
                    jsonText = inputMapper.writeValueAsString(newTask);

                    Entity<String> payload 
                            = Entity.json(
                                    jsonText
                                    );

                    response = client.target(httpToCall)
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

                        ObjectMapper outputMapper = new ObjectMapper();
                        Task task = outputMapper.readValue(body, Task.class);
                        return task;

                    } else {
                        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                    }
                } catch(JsonParseException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(JsonMappingException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } finally{
                    if( response != null ) {
                        response.close();
                    }
                    if( client != null ) {
                        client.close();
                    }
                }
                
                return null;
	}
	
	/**
         * Submit task
	 * 
         */
        public Upload uploadFileForTask(Task task, String inputURL, File file) throws Exception {

                String httpToCall = tasksHttpAddress + "/" + task.getId() + "/input?user=" + task.getUser();

                LOGGER.info("Calling: " + httpToCall);
                
                FileDataBodyPart    fileDataBodyPart = null;
                MultiPart           multiPart = null;
                Response            response = null;
                Client              client = null;
                
                try {
                    client = ClientBuilder.newBuilder()
                                    .register(MultiPartFeature.class).build();
                    client.register(new LoggingFilter());
                    WebTarget webTarget = client.target(httpToCall);
                    multiPart = new MultiPart();
                    multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

                    fileDataBodyPart = 
                            new FileDataBodyPart(
                                    "file[]",
                                    file,
                                    MediaType.APPLICATION_OCTET_STREAM_TYPE);

                    multiPart.bodyPart(fileDataBodyPart);

                    response = 
                               webTarget
                                       .request(MediaType.APPLICATION_JSON_TYPE)
                                       .post( Entity.entity(
                                                multiPart, 
                                                multiPart.getMediaType()));

                    int status = response.getStatus();

                    LOGGER.info("Status:" + status);

                    if(status == 200) {
                        MultivaluedMap<String,Object> headers = response.getHeaders();
                        String body = response.readEntity(String.class);

                        LOGGER.info("Body: " + body);

                        ObjectMapper mapper = new ObjectMapper();
                        Upload upload = mapper.readValue(body, Upload.class);
                        return upload;
                        
                    } else {
                        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                        return null;
                    }
                } catch(JsonParseException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(JsonMappingException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } finally{
                    if( fileDataBodyPart != null ) {
                        fileDataBodyPart.cleanup();
                    }
                    if( multiPart != null ) {
                        multiPart.cleanup();
                    }
                    if( response != null ) {
                        response.close();
                    }
                    if( client != null ) {
                        client.close();
                    }
                }
                // in case we have failed to connect/parse response, we will
                return null;
        }

	/**
         * Check status
         * 
         */
        public Task getTask(Task task) throws Exception {

                String httpToCall = tasksHttpAddress + "/" + task.getId();
                LOGGER.info("Calling: " + httpToCall);
                
                Client      client = null;
                Response    response = null;
            
                try {
                    client = ClientBuilder.newClient();

                    Entity<String> payload
                            = Entity.text( "" );

                    response = client.target(httpToCall)
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
                        ObjectMapper mapper = new ObjectMapper();
                        Task retVal = mapper.readValue(body, Task.class);
                        return retVal;
                    } else {
                      LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                      return null;  
                    }
                } catch(JsonParseException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(JsonMappingException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } finally{
                    if( response != null ) {
                        response.close();
                    }
                    if( client != null ) {
                        client.close();
                    }
                }
                return null;
        }
        
        public Task[] getAllTasks() throws Exception {
            
                String httpToCall = tasksHttpAddress;
                LOGGER.info("Calling: " + httpToCall);
                
                Client client = null;
                Response response = null;
            
                try {
                    client = ClientBuilder.newClient();

                    Entity<String> payload
                            = Entity.text( "" );

                    response = client.target(httpToCall)
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
                        ObjectMapper mapper = new ObjectMapper();
                        Task [] tasks = mapper.readValue(body, Task[].class);
                        return tasks;
                    } else {
                        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
                        ObjectMapper mapper = new ObjectMapper();
                        ErrorMessage message = mapper.readValue(body, ErrorMessage.class);
                        throw new Exception("Error message: " + message.getMessage());
                    }
                } catch(JsonParseException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(JsonMappingException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } catch(Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
                } finally{
                    if( response != null ) {
                        response.close();
                    }
                    if( client != null ) {
                        client.close();
                    }
                }
                return null;
        }
        
}	
