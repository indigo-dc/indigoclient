package pl.psnc.indigo.fg.api.restful;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TasksAPI extends BaseAPI {
    private final static Logger LOGGER = Logger.getLogger(TasksAPI.class.getName());

    private ObjectMapper mapper = new ObjectMapper();

    private final String tasksHttpAddress;

    public TasksAPI(String httpAddress) throws FutureGatewayException {
        super(httpAddress);
        tasksHttpAddress = RootAPI.getRootForAddress(httpAddress).getURLAsString() + "tasks";

        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Calls create task at server side
     * <p>
     * To submit task we have to pass Task object filled with description of the
     * task - user - application's id - arguments - description - input files -
     * output files
     * <p>
     * The set of parameters might be application dependant. For example some
     * applications might require inputs and some other, not.
     */
    public Task createTask(Task task) throws FutureGatewayException {
        String httpToCall = tasksHttpAddress + "?user=" + task.getUser();
        LOGGER.info("Calling: " + httpToCall);

        Client client = null;
        Response response = null;

        try {
            Entity<String> payload = Entity.json(mapper.writeValueAsString(task));

            client = ClientBuilder.newClient();
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer {access_token}")
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .post(payload);
            int status = response.getStatus();
            LOGGER.info("Response status: " + status);

            if (status == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.info("Body: " + body);
                return mapper.readValue(body, Task.class);
            } else {
                String message = "Failed to create task. Response: " + response.getStatus() + " " + response + "\nTask: " + task;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to create task\nTask: " + task;
            LOGGER.log(Level.SEVERE, message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Upload file for task
     */
    public Upload uploadFileForTask(Task task, File file) throws FutureGatewayException {
        String httpToCall = tasksHttpAddress + "/" + task.getId() + "/input?user=" + task.getUser();
        LOGGER.info("Calling: " + httpToCall);

        FileDataBodyPart fileDataBodyPart = null;
        MultiPart multiPart = null;
        Response response = null;
        Client client = null;

        try {
            client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            client.register(new LoggingFilter());
            WebTarget webTarget = client.target(httpToCall);
            multiPart = new MultiPart();
            multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

            fileDataBodyPart = new FileDataBodyPart("file[]", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
            multiPart.bodyPart(fileDataBodyPart);

            response = webTarget
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(multiPart, multiPart.getMediaType()));
            int status = response.getStatus();
            LOGGER.info("Status:" + status);

            if (status == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.info("Body: " + body);
                return mapper.readValue(body, Upload.class);
            } else {
                String message = "Failed to upload file for task. Response: " + response.getStatus() + " " + response + "\nTask: " + task + "\nFile: " + file;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to upload file for task\nTask: " + task + "\nFile: " + file;
            LOGGER.log(Level.SEVERE, message, e);
            throw new FutureGatewayException(message, e);
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
    }

    /**
     * Check status
     */
    public Task getTask(Task task) throws FutureGatewayException {
        String httpToCall = tasksHttpAddress + "/" + task.getId();
        LOGGER.info("Calling: " + httpToCall);

        Client client = null;
        Response response = null;

        try {
            client = ClientBuilder.newClient();
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer {access_token}")
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);
            int status = response.getStatus();
            LOGGER.info("Status:" + status);

            if (status == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.info("Body: " + body);
                return mapper.readValue(body, Task.class);
            } else {
                String message = "Failed to get task. Response: " + response.getStatus() + " " + response + "\nTask: " + task;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException ex) {
            String message = "Failed to get task\nTask: " + task;
            LOGGER.severe(message);
            throw new FutureGatewayException(message);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Get output files for task
     */
    public List<OutputFile> getOutputsForTask(Task task) throws FutureGatewayException {
        String httpToCall = tasksHttpAddress + "/" + task.getId();
        LOGGER.info("Calling: " + httpToCall);

        Client client = null;
        Response response = null;

        try {
            client = ClientBuilder.newClient();
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer {access_token}")
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);
            int status = response.getStatus();
            LOGGER.info("Status:" + status);

            if (status == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.info("Body: " + body);
                Task retVal = mapper.readValue(body, Task.class);

                // There is no sense to get output files for tasks that are not DONE
                // In case of tasks that are still running we will get the same url
                // that is: "url": "file?path=&name=sayhello.out"
                if (retVal.getStatus() != Task.Status.DONE || retVal.getOutputFiles() == null) {
                    return Collections.emptyList();
                }

                return Collections.unmodifiableList(retVal.getOutputFiles());
            } else {
                String message = "Failed to get output files for task. Response: " + response.getStatus() + " " + response + "\nTask: " + task;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get output files for task\nTask: " + task;
            LOGGER.severe(message);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Gets output file for the job
     */
    public void downloadOutputFile(OutputFile outputFile, File directory) throws FutureGatewayException {
        String httpToCall = RootAPI.getRootForAddress(BaseAPI.LOCALHOST_ADDRESS).getURLAsString() + outputFile.getUrl();
        LOGGER.info("Calling: " + httpToCall);

        Client client = null;
        Response response = null;

        try {
            client = ClientBuilder.newClient();
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer {access_token}")
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .get(Response.class);
            int status = response.getStatus();
            LOGGER.info("Status:" + status);

            if (status == Response.Status.OK.getStatusCode()) {
                InputStream is = response.readEntity(InputStream.class);
                Files.copy(is, new File(directory, outputFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                String message = "Failed to download file. Response: " + response.getStatus() + " " + response + "\nOutput file: " + outputFile + "\nDirectory: " + directory;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to download file\nOutput file: " + outputFile + "\nDirectory: " + directory;
            LOGGER.severe(message);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    public List<Task> getAllTasks() throws FutureGatewayException {
        String httpToCall = tasksHttpAddress;
        LOGGER.info("Calling: " + httpToCall);

        Client client = null;
        Response response = null;

        try {
            client = ClientBuilder.newClient();
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer {access_token}")
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);
            int status = response.getStatus();
            LOGGER.info("Status:" + status);

            if (status == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.info("Body: " + body);
                return Arrays.asList(mapper.readValue(body, Task[].class));
            } else {
                String message = "Failed to get all tasks. Response: " + response.getStatus() + " " + response;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get all tasks";
            LOGGER.severe(message);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }
}
