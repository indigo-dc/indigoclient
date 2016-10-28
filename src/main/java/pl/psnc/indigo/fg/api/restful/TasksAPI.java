package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.TaskStatus;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
@Slf4j
public class TasksAPI extends RootAPI {
    private static final String TASKS = "tasks";

    private final URI tasksUri;

    /**
     * Construct an instance which allows to communicate with Future Gateway
     * using non-default {@link Client}.
     *
     * @param baseUri            Base URI of Future Gateway i.e.
     *                           protocol://host:port
     * @param client             Implementation of REST client.
     * @param authorizationToken Token which identifies the user to services.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public TasksAPI(final URI baseUri, final Client client,
                    final String authorizationToken)
            throws FutureGatewayException {
        super(baseUri, client, authorizationToken);

        URI rootUri = getRootUri();
        tasksUri = UriBuilder.fromUri(rootUri).path(TasksAPI.TASKS).build();
    }

    /**
     * Construct an instance which allows to communicate with Future Gateway.
     *
     * @param baseUri            Base URI of Future Gateway i.e.
     *                           protocol://host:port
     * @param authorizationToken Token which identifies the user to services.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public TasksAPI(final URI baseUri, final String authorizationToken)
            throws FutureGatewayException {
        super(baseUri, authorizationToken);

        URI rootUri = getRootUri();
        tasksUri = UriBuilder.fromUri(rootUri).path(TasksAPI.TASKS).build();
    }

    /**
     * Creates a task on Future Gateway.
     * <p>
     * To submit task we have to pass Task object filled with description of the
     * task: user, application id, arguments, description, input files, output
     * files
     * <p>
     * The set of parameters might be application dependant. For example some
     * applications might require inputs and some other, not.
     *
     * @param task A bean containing all information about the task.
     *
     * @return A bean with details about task submission.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Task createTask(final Task task)
            throws FutureGatewayException {
        Entity<String> entity;
        try {
            String taskJson = getMapper().writeValueAsString(task);
            entity = Entity.json(taskJson);
        } catch (JsonProcessingException e) {
            throw new FutureGatewayException("Failed to prepare JSON for task",
                                             e);
        }

        String user = task.getUser();
        URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", user).build();
        TasksAPI.log.debug("POST {}", uri);
        Response response =
                getClient().target(uri).request(MediaType.APPLICATION_JSON_TYPE)
                           .accept(MediaType.APPLICATION_JSON_TYPE)
                           .header(HttpHeaders.CONTENT_TYPE,
                                   MediaType.APPLICATION_JSON)
                           .header(HttpHeaders.AUTHORIZATION,
                                   getAuthorizationToken()).post(entity);

        Response.StatusType status = response.getStatusInfo();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();
        TasksAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message = "Failed to create task. Response: " + response
                        .getStatus() + ' ' + response + " Task: " + task;
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to create task: " + task;
            TasksAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            response.close();
        }
    }

    /**
     * Upload an input file for task.
     *
     * @param task A bean describing task id and user.
     * @param file A file to upload.
     *
     * @return A bean containing status information about the uploaded file.
     *
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

        try (MultiPart multiPart = new MultiPart()) {
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
            fileDataBodyPart = new FileDataBodyPart("file[]", file, mediaType);

            mediaType = MediaType.MULTIPART_FORM_DATA_TYPE;
            multiPart.setMediaType(mediaType);
            multiPart.bodyPart(fileDataBodyPart);
            Entity<MultiPart> entity = Entity.entity(multiPart, mediaType);

            TasksAPI.log.debug("POST {}", uri);
            mediaType = MediaType.APPLICATION_JSON_TYPE;
            Response response =
                    getClient().target(uri).request(mediaType).accept(mediaType)
                               .header(HttpHeaders.AUTHORIZATION,
                                       getAuthorizationToken()).post(entity);

            Response.StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                return getMapper().readValue(body, Upload.class);
            } else {
                String message =
                        "Failed to upload file for task. Response: " + response
                                .getStatus() + ' ' + response + " Task: " + task
                        + " File: " + file;
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message =
                    "Failed to upload file for task: " + task + " File: "
                    + file;
            TasksAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (fileDataBodyPart != null) {
                fileDataBodyPart.cleanup();
            }
        }

    }

    /**
     * Get details of a task.
     *
     * @param id Id of a task as returned from Future Gateway during its
     *           creation.
     *
     * @return A bean with details about a task.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Task getTask(final String id) throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).path(id).build();

        TasksAPI.log.debug("GET {}", uri);
        Response response =
                getClient().target(uri).request(MediaType.APPLICATION_JSON_TYPE)
                           .accept(MediaType.APPLICATION_JSON_TYPE)
                           .header(HttpHeaders.CONTENT_TYPE,
                                   MediaType.APPLICATION_JSON)
                           .header(HttpHeaders.AUTHORIZATION,
                                   getAuthorizationToken()).get();

        Response.StatusType status = response.getStatusInfo();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();
        TasksAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message =
                        "Failed to get task. Response: " + response.getStatus()
                        + ' ' + response + " Task: " + id;
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get task: " + id;
            TasksAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            response.close();
        }
    }

    /**
     * Get collection of output files' information. This method returns any
     * information only when task status is DONE.
     *
     * @param id An id of a task.
     *
     * @return A list of beans with information about task output files.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final List<OutputFile> getOutputsForTask(final String id)
            throws FutureGatewayException {
        Task task = getTask(id);

        // There is no sense to get output files for tasks that are not DONE
        // In case of tasks that are still running we will get the same url
        // that is: "url": "file?path=&name=sayhello.out"
        if (task.getStatus() != TaskStatus.DONE) {
            return Collections.emptyList();
        }

        return task.getOutputFiles();
    }

    /**
     * Downloads a single output file to a provided directory.
     *
     * @param outputFile A bean describing task output file.
     * @param directory  A directory where file will be downloaded.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final void downloadOutputFile(final OutputFile outputFile,
                                         final File directory)
            throws FutureGatewayException {
        try {
            TasksAPI.testLocalFolderPermissions(directory);
        } catch (IOException e) {
            throw new FutureGatewayException("Failed to download output file",
                                             e);
        }

        URI outputFileUri = outputFile.getUrl();
        String path = outputFileUri.getPath();
        String query = outputFileUri.getQuery();
        URI rootUri = getRootUri();
        URI uri = UriBuilder.fromUri(rootUri).path(path).replaceQuery(query)
                            .build();

        TasksAPI.log.debug("GET {}", uri);
        Response response = getClient().target(uri).request()
                                       .header(HttpHeaders.AUTHORIZATION,
                                               getAuthorizationToken()).get();

        Response.StatusType status = response.getStatusInfo();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();
        TasksAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                InputStream is = response.readEntity(InputStream.class);
                String name = outputFile.getName();
                Path filePath = new File(directory, name).toPath();
                Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                String message =
                        "Failed to download file. Response: " + response
                                .getStatus() + ' ' + response + " Output file: "
                        + outputFile + " Directory: " + directory;
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            throw new FutureGatewayException("Failed to download file", e);
        } finally {
            response.close();
        }
    }

    /**
     * Check if given path points to a valid directory where outputs can be
     * downloaded.
     *
     * @param localFolder A path on local drive.
     *
     * @throws IOException If anything is found to be wrong in the given path.
     */
    private static void testLocalFolderPermissions(final File localFolder)
            throws IOException {
        if (localFolder.exists()) {
            if (!localFolder.isDirectory()) {
                throw new IOException(
                        "Output path exists and is " + "not a directory: "
                        + localFolder);
            }
            if (!localFolder.canWrite()) {
                throw new IOException("Cannot write to: " + localFolder);
            }
        } else {
            if (!localFolder.mkdirs()) {
                throw new IOException(
                        "Failed to create directory: " + localFolder);
            }
        }
    }

    /**
     * Get all tasks belonging to a user.
     *
     * @param user An id of a user.
     *
     * @return A collection of tasks belonging to a user.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final List<Task> getAllTasks(final String user)
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", user).build();

        TasksAPI.log.debug("GET {}", uri);
        Response response =
                getClient().target(uri).request(MediaType.APPLICATION_JSON_TYPE)
                           .accept(MediaType.APPLICATION_JSON_TYPE)
                           .header(HttpHeaders.CONTENT_TYPE,
                                   MediaType.APPLICATION_JSON)
                           .header(HttpHeaders.AUTHORIZATION,
                                   getAuthorizationToken()).get();

        Response.StatusType status = response.getStatusInfo();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();
        TasksAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                JsonNode jsonNode = getMapper().readTree(body);
                jsonNode = jsonNode.get("tasks");
                Task[] tasks = getMapper().treeToValue(jsonNode, Task[].class);
                return Arrays.asList(tasks);
            } else {
                String message =
                        "Failed to get all tasks. Response: " + response
                                .getStatus() + ' ' + response;
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get all tasks";
            TasksAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            response.close();
        }
    }

    /**
     * Delete a single task.
     *
     * @param id An id of a task.
     *
     * @return Whether deletion was successful.
     */
    public final boolean deleteTask(final String id) {
        URI uri = UriBuilder.fromUri(tasksUri).path(id).build();

        TasksAPI.log.debug("DELETE {}", uri);
        Response response = getClient().target(uri).request()
                                       .header(HttpHeaders.AUTHORIZATION,
                                               getAuthorizationToken())
                                       .delete();

        try {
            Response.StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);
            return status.getFamily() == Response.Status.Family.SUCCESSFUL;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
