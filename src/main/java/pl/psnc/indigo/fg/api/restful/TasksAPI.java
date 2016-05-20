package pl.psnc.indigo.fg.api.restful;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
import javax.ws.rs.client.WebTarget;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.glassfish.jersey.filter.LoggingFilter;
import pl.psnc.indigo.fg.api.restful.jaxb.ErrorMessage;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;

public class TasksAPI extends BaseAPI {

  private final static Logger LOGGER = Logger.getLogger(TasksAPI.class.getName());
  String tasksHttpAddress = null;
  
  public static final String DONE = "DONE";
  public static final String SUBMIT = "SUBMIT";
  public static final String SUBMITTED = "SUBMITTED";
  public static final String WAITING = "WAITING";  

  public TasksAPI(String httpAddress) {
    super(httpAddress);
    tasksHttpAddress = RootAPI.getRootForAddress(httpAddress).getURLAsString() + "tasks";
  }

  /**
   * Calls create task at server side
   *
   * To submit task we have to pass Task object filled with description of the
   * task - user - application's id - arguments - description - input files -
   * output files
   *
   * The set of parameters might be application dependant. For example some
   * applications might require inputs and some other, not.
   */
  public Task createTask(Task newTask) throws Exception {

    String httpToCall = tasksHttpAddress + "?user=" + newTask.getUser();
    LOGGER.info("Calling: " + httpToCall);

    Client client = null;
    Response response = null;

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
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer {access_token}")
        .accept(MediaType.APPLICATION_JSON_TYPE)
        .post(payload);
      int status = response.getStatus();

      if (status == 200) {
        LOGGER.info("Response status: " + status);
        MultivaluedMap<String, Object> headers = response.getHeaders();
        String body = response.readEntity(String.class);

        LOGGER.info("Body: " + body);

        ObjectMapper outputMapper = new ObjectMapper();
        Task task = outputMapper.readValue(body, Task.class);
        return task;

      } else {
        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
      }
    } catch (JsonParseException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (JsonMappingException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } finally {
      if (response != null) {
        response.close();
      }
      if (client != null) {
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

    FileDataBodyPart fileDataBodyPart = null;
    MultiPart multiPart = null;
    Response response = null;
    Client client = null;

    try {
      client = ClientBuilder.newBuilder()
        .register(MultiPartFeature.class).build();
      client.register(new LoggingFilter());
      WebTarget webTarget = client.target(httpToCall);
      multiPart = new MultiPart();
      multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

      fileDataBodyPart
        = new FileDataBodyPart(
          "file[]",
          file,
          MediaType.APPLICATION_OCTET_STREAM_TYPE);

      multiPart.bodyPart(fileDataBodyPart);

      response
        = webTarget
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(
          multiPart,
          multiPart.getMediaType()));

      int status = response.getStatus();

      LOGGER.info("Status:" + status);

      if (status == 200) {
        MultivaluedMap<String, Object> headers = response.getHeaders();
        String body = response.readEntity(String.class);

        LOGGER.info("Body: " + body);

        ObjectMapper mapper = new ObjectMapper();
        Upload upload = mapper.readValue(body, Upload.class);
        return upload;

      } else {
        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
        return null;
      }
    } catch (JsonParseException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (JsonMappingException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } finally {
      if (fileDataBodyPart != null) {
        fileDataBodyPart.cleanup();
      }
      if (multiPart != null) {
        multiPart.cleanup();
      }
      if (response != null) {
        response.close();
      }
      if (client != null) {
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

    Client client = null;
    Response response = null;

    try {
      client = ClientBuilder.newClient();

      Entity<String> payload
        = Entity.text("");

      response = client.target(httpToCall)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer {access_token}")
        .accept(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);
      int status = response.getStatus();

      LOGGER.info("Status:" + status);

      MultivaluedMap<String, Object> headers = response.getHeaders();
      String body = response.readEntity(String.class);

      LOGGER.info("Body: " + body);

      if (status == 200) {
        ObjectMapper mapper = new ObjectMapper();
        Task retVal = mapper.readValue(body, Task.class);
        return retVal;
      } else {
        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
        return null;
      }
    } catch (JsonParseException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (JsonMappingException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } finally {
      if (response != null) {
        response.close();
      }
      if (client != null) {
        client.close();
      }
    }
    return null;
  }
  
  /**
   * Check status
   *
   */
  public ArrayList<OutputFile> getOutputsForTask(Task task) throws Exception {

    ArrayList<OutputFile> outputFilesArray = new ArrayList<OutputFile>();
    
    String httpToCall = tasksHttpAddress + "/" + task.getId();
    LOGGER.info("Calling: " + httpToCall);

    Client client = null;
    Response response = null;

    try {
      client = ClientBuilder.newClient();

      Entity<String> payload
        = Entity.text("");

      response = client.target(httpToCall)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer {access_token}")
        .accept(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);
      int status = response.getStatus();

      LOGGER.info("Status:" + status);

      MultivaluedMap<String, Object> headers = response.getHeaders();
      String body = response.readEntity(String.class);

      LOGGER.info("Body: " + body);

      if (status == 200) {
        ObjectMapper mapper = new ObjectMapper();
        Task retVal = mapper.readValue(body, Task.class);
        
        // There is no sense to get output files for tasks that are not DONE
        // In case of tasks that are still running we will get the same url
        // that is: "url": "file?path=&name=sayhello.out"
        if( retVal.getStatus().equals(TasksAPI.DONE)) {
          if( retVal.getOutputFiles() != null ) {
            for(int i = 0; i<retVal.getOutputFiles().size(); i++) {
               OutputFile file = retVal.getOutputFiles().get(i);
               outputFilesArray.add(file);
            }
          }
        }
        return outputFilesArray;
        
      } else {
        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
        return null;
      }
    } catch (JsonParseException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (JsonMappingException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } finally {
      if (response != null) {
        response.close();
      }
      if (client != null) {
        client.close();
      }
    }
    return null;
  }
  
  /**
   * Gets output file for the job
   * @param url location of the file at WS server; it can be retrieved by
   *            calling {@link #getOutputsForTask(Task task) getOutputsForTask}
   * @param localFile full path of the local file - place where we want to store
   *                  output
   * @return true - in case download was successful, false - if it failed
   * @throws Exception 
   */
  public boolean downloadOutputFile(OutputFile file, String folder) throws Exception {

    String httpToCall = RootAPI.getRootForAddress(BaseAPI.LOCALHOST_ADDRESS).getURLAsString() + file.getUrl();
    LOGGER.info("Calling: " + httpToCall);

    Client client = null;
    Response response = null;

    try {
      client = ClientBuilder.newClient();

      Entity<String> payload
        = Entity.text("");

      response = client.target(httpToCall)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer {access_token}")
        .accept(MediaType.APPLICATION_OCTET_STREAM)
        .get(Response.class);
      int status = response.getStatus();

      LOGGER.info("Status:" + status);

      MultivaluedMap<String, Object> headers = response.getHeaders();
//      String body = response.readEntity(String.class);

//      LOGGER.info("Body: " + body);

      if (status == 200) {
        
        InputStream is = response.readEntity(InputStream.class);
        Files.copy(is, new File(folder + "/" + file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        
//        try(  PrintWriter out = new PrintWriter( folder + "/" + file.getName() )  ){
//          out.println( body );
//        }
        return true;
      } else {
        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
        return false;
      }
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } finally {
      if (response != null) {
        response.close();
      }
      if (client != null) {
        client.close();
      }
    }
    return false;
  }

  public Task[] getAllTasks() throws Exception {

    String httpToCall = tasksHttpAddress;
    LOGGER.info("Calling: " + httpToCall);

    Client client = null;
    Response response = null;

    try {
      client = ClientBuilder.newClient();

      Entity<String> payload
        = Entity.text("");

      response = client.target(httpToCall)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer {access_token}")
        .accept(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);
      int status = response.getStatus();
      LOGGER.info("Status:" + status);
      MultivaluedMap<String, Object> headers = response.getHeaders();
      String body = response.readEntity(String.class);

      LOGGER.info("Body: " + body);

      if (status == 200) {
        ObjectMapper mapper = new ObjectMapper();
        Task[] tasks = mapper.readValue(body, Task[].class);
        return tasks;
      } else {
        LOGGER.severe("Error while calling: " + httpToCall + " - status: " + status);
        ObjectMapper mapper = new ObjectMapper();
        ErrorMessage message = mapper.readValue(body, ErrorMessage.class);
        throw new Exception("Error message: " + message.getMessage());
      }
    } catch (JsonParseException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (JsonMappingException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } finally {
      if (response != null) {
        response.close();
      }
      if (client != null) {
        client.close();
      }
    }
    return null;
  }

}
