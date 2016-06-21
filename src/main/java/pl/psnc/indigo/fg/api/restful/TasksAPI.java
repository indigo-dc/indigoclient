package pl.psnc.indigo.fg.api.restful;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Allows to manipulate tasks via Future Gateway: list, submit, delete.
 */
public class TasksAPI extends RootAPI {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TasksAPI.class);
    private static final String TASKS = "tasks";

    private final URI tasksUri;

    /**
     * Construct an instance which allows to communicate with Future Gateway.
     *
     * @param baseUri Base URI of Future Gateway i.e. protocol://host:port
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public TasksAPI(final String baseUri) throws FutureGatewayException {
        super(baseUri);

        URI rootUri = getRootUri();
        tasksUri = UriBuilder.fromUri(rootUri).path(TasksAPI.TASKS).build();
    }

    /**
     * Creates a task on Future Gateway.
     * <p>
     * To submit task we have to pass Task object filled with description of the
     * task: user, application id, arguments, description, input files,
     * output files
     * <p>
     * The set of parameters might be application dependant. For example some
     * applications might require inputs and some other, not.
     *
     * @param task A bean containing all information about the task.
     * @return A bean with details about task submission.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Task createTask(final Task task)
            throws FutureGatewayException {
        String user = task.getUser();
        URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", user).build();
        Response response = null;

        try {
            TasksAPI.LOGGER.debug("POST {}", uri);
            String taskJson = getMapper().writeValueAsString(task);
            Entity<String> entity = Entity.json(taskJson);
            response = getClient().target(uri)
                                  .request(MediaType.APPLICATION_JSON_TYPE)
                                  .accept(MediaType.APPLICATION_JSON_TYPE)
                                  .header(HttpHeaders.CONTENT_TYPE,
                                          MediaType.APPLICATION_JSON)
                                  .header(HttpHeaders.AUTHORIZATION,
                                          "Bearer {access_token}").post(entity);

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.LOGGER.trace("Body: {}", body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message = "Failed to create task. Response: " + response
                        .getStatus() + ' ' + response + " Task: " + task;
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to create task: " + task;
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Upload an input file for task.
     *
     * @param task A bean describing task id and user.
     * @param file A file to upload.
     * @return A bean containing status information about the uploaded file.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Upload uploadFileForTask(final Task task, final File file)
            throws FutureGatewayException {
        String id = task.getId();
        String user = task.getUser();
        URI uri = UriBuilder.fromUri(tasksUri).path(id).path("input")
                            .queryParam("user", user).build();
        FileDataBodyPart fileDataBodyPart = null;
        Response response = null;

        try (MultiPart multiPart = new MultiPart()) {
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
            fileDataBodyPart = new FileDataBodyPart("file[]", file, mediaType);

            multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
            multiPart.bodyPart(fileDataBodyPart);

            TasksAPI.LOGGER.debug("POST {}", uri);
            Entity<MultiPart> entity = Entity
                    .entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE);
            response = getClient().target(uri)
                                  .request(MediaType.APPLICATION_JSON_TYPE)
                                  .accept(MediaType.APPLICATION_JSON_TYPE)
                                  .post(entity);

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.LOGGER.trace("Body: {}", body);
                return getMapper().readValue(body, Upload.class);
            } else {
                String message = "Failed to upload file for task. Response: "
                                 + response.getStatus() + ' ' + response
                                 + " Task: " + task + " File: " + file;
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to upload file for task: " + task
                             + " File: " + file;
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (fileDataBodyPart != null) {
                fileDataBodyPart.cleanup();
            }
            if (response != null) {
                response.close();
            }
        }

    }

    /**
     * Get details of a task.
     *
     * @param id Id of a task as returned from Future Gateway during its
     *           creation.
     * @return A bean with details about a task.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Task getTask(final String id) throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).path(id).build();
        Response response = null;

        try {
            TasksAPI.LOGGER.debug("GET {}", uri);
            response = getClient().target(uri)
                                  .request(MediaType.APPLICATION_JSON_TYPE)
                                  .accept(MediaType.APPLICATION_JSON_TYPE)
                                  .header(HttpHeaders.CONTENT_TYPE,
                                          MediaType.APPLICATION_JSON)
                                  .header(HttpHeaders.AUTHORIZATION,
                                          "Bearer {access_token}").get();

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.LOGGER.trace("Body: {}", body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message = "Failed to get task. Response: " + response
                        .getStatus() + ' ' + response + " Task: " + id;
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get task: " + id;
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Get collection of output files' information. This method returns any
     * information only when task status is DONE.
     *
     * @param id An id of a task.
     * @return A list of beans with information about task output files.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final List<OutputFile> getOutputsForTask(final String id)
            throws FutureGatewayException {
        Task task = getTask(id);

        // There is no sense to get output files for tasks that are not DONE
        // In case of tasks that are still running we will get the same url
        // that is: "url": "file?path=&name=sayhello.out"
        if (task.getStatus() != Task.Status.DONE) {
            return Collections.emptyList();
        }

        return task.getOutputFiles();
    }

    /**
     * Downloads a single output file to a provided directory.
     *
     * @param outputFile A bean describing task output file.
     * @param directory  A directory where file will be downloaded.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final void downloadOutputFile(final OutputFile outputFile,
                                         final File directory)
            throws FutureGatewayException {
        URI outputFileUri = outputFile.getUrl();
        String path = outputFileUri.getPath();
        String query = outputFileUri.getQuery();
        URI rootUri = getRootUri();
        URI uri = UriBuilder.fromUri(rootUri).path(path).replaceQuery(query)
                            .build();
        Response response = null;

        try {
            TasksAPI.LOGGER.debug("GET {}", uri);
            response = getClient().target(uri)
                                  .request(MediaType.APPLICATION_JSON_TYPE)
                                  .accept(MediaType.APPLICATION_OCTET_STREAM)
                                  .header(HttpHeaders.CONTENT_TYPE,
                                          MediaType.APPLICATION_JSON)
                                  .header(HttpHeaders.AUTHORIZATION,
                                          "Bearer {access_token}").get();

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                InputStream is = response.readEntity(InputStream.class);
                String name = outputFile.getName();
                Path filePath = new File(directory, name).toPath();
                Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                String message = "Failed to download file. Response: "
                                 + response.getStatus() + ' ' + response
                                 + " Output file: " + outputFile
                                 + " Directory: " + directory;
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to download file: " + outputFile
                             + " Directory: " + directory;
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Get all tasks belonging to a user.
     *
     * @param user An id of a user.
     * @return A collection of tasks belonging to a user.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Iterable<Task> getAllTasks(final String user)
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", user).build();
        Response response = null;

        try {
            TasksAPI.LOGGER.debug("GET {}", uri);
            response = getClient().target(uri)
                                  .request(MediaType.APPLICATION_JSON_TYPE)
                                  .accept(MediaType.APPLICATION_JSON_TYPE)
                                  .header(HttpHeaders.CONTENT_TYPE,
                                          MediaType.APPLICATION_JSON)
                                  .header(HttpHeaders.AUTHORIZATION,
                                          "Bearer {access_token}").get();

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.LOGGER.trace("Body: {}", body);
                Task[] tasks = getMapper().readValue(body, Task[].class);
                return Arrays.asList(tasks);
            } else {
                String message = "Failed to get all tasks. Response: "
                                 + response.getStatus() + ' ' + response;
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get all tasks";
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Delete a single task.
     *
     * @param id An id of a task.
     * @return Whether deletion was successful.
     */
    public final boolean deleteTask(final String id) {
        URI uri = UriBuilder.fromUri(tasksUri).path(id).build();
        Response response = null;

        try {
            TasksAPI.LOGGER.debug("DELETE {}", uri);
            response = getClient().target(uri).request().delete();

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug("Status: {} {}", statusCode, reasonPhrase);
            return status.getFamily() == Family.SUCCESSFUL;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this).append("tasksUri", tasksUri)
                                        .toString();
    }
}
