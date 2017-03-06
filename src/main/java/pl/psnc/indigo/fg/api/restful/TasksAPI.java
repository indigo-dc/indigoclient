package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.PatchRuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
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
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Allows to manipulate tasks via Future Gateway: list, submit, delete.
 */
@Slf4j
public class TasksAPI extends RootAPI {
    private static final String TASKS = "tasks";

    private final ResourceBundle resourceBundle =
            ResourceBundle.getBundle("messages"); //NON-NLS

    private final URI tasksUri;

    /**
     * Construct an instance which allows to communicate with Future Gateway
     * using non-default {@link Client}.
     *
     * @param baseUri            Base URI of Future Gateway i.e.
     *                           protocol://host:port
     * @param client             Implementation of REST client.
     * @param authorizationToken Token which identifies the user to services.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public TasksAPI(
            final URI baseUri, final Client client,
            final String authorizationToken) throws FutureGatewayException {
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
     * @return A bean with details about task submission.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Task createTask(final Task task)
            throws FutureGatewayException {
        Entity<String> entity;
        try {
            String taskJson = getMapper().writeValueAsString(task);
            entity = Entity.json(taskJson);
        } catch (final JsonProcessingException e) {
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
        TasksAPI.log.debug(RootAPI.STATUS, statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message = resourceBundle
                        .getString("failed.to.create.task.response.0.1.task.2");
                message = MessageFormat
                        .format(message, statusCode, response, task);
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message =
                    resourceBundle.getString("failed.to.create.task.0");
            message = MessageFormat.format(message, task);
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

        try (MultiPart multiPart = new MultiPart()) {
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
            fileDataBodyPart =
                    new FileDataBodyPart("file[]", file, mediaType); //NON-NLS

            MediaType multipartFormDataType =
                    MediaType.MULTIPART_FORM_DATA_TYPE;
            multiPart.setMediaType(multipartFormDataType);
            multiPart.bodyPart(fileDataBodyPart);
            Entity<MultiPart> entity =
                    Entity.entity(multiPart, multipartFormDataType);

            TasksAPI.log.debug("POST {}", uri);
            MediaType applicationJsonType = MediaType.APPLICATION_JSON_TYPE;
            Response response =
                    getClient().target(uri).request(applicationJsonType)
                               .accept(applicationJsonType)
                               .header(HttpHeaders.AUTHORIZATION,
                                       getAuthorizationToken()).post(entity);

            Response.StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.log.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                return getMapper().readValue(body, Upload.class);
            } else {
                String message = resourceBundle.getString(
                        "failed.to.upload.file.for.task.response.0.1.task.2.3");
                message = MessageFormat
                        .format(message, statusCode, response, task, file);
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message = resourceBundle
                    .getString("failed.to.upload.file.for.task.0.file.1");
            message = MessageFormat.format(message, task, file);
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
     * @return A bean with details about a task.
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
        TasksAPI.log.debug(RootAPI.STATUS, statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message = resourceBundle
                        .getString("failed.to.get.task.response.0.1.task.2");
                message =
                        MessageFormat.format(message, statusCode, response, id);
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message = resourceBundle.getString("failed.to.get.task.0");
            message = MessageFormat.format(message, id);
            TasksAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            response.close();
        }
    }

    /**
     * Downloads a single output file to a provided directory.
     *
     * @param outputFile A bean describing task output file.
     * @param directory  A directory where file will be downloaded.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final void downloadOutputFile(
            final OutputFile outputFile, final File directory)
            throws FutureGatewayException {
        try {
            TasksAPI.testLocalFolderPermissions(directory);
        } catch (final IOException e) {
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
        TasksAPI.log.debug(RootAPI.STATUS, statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                InputStream is = response.readEntity(InputStream.class);
                String name = outputFile.getName();
                Path filePath = new File(directory, name).toPath();
                Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                String message = resourceBundle.getString(
                        "failed.to.download.file.response.0.1.output.file.2.3");
                message = MessageFormat
                        .format(message, statusCode, response, outputFile,
                                directory);
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
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
     * @return A collection of tasks belonging to a user.
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
        TasksAPI.log.debug(RootAPI.STATUS, statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                TasksAPI.log.trace("Body: {}", body);
                JsonNode jsonNode = getMapper().readTree(body);
                jsonNode = jsonNode.get("tasks"); //NON-NLS
                Task[] tasks = getMapper().treeToValue(jsonNode, Task[].class);
                return Arrays.asList(tasks);
            } else {
                String message = resourceBundle
                        .getString("failed.to.get.all.tasks.response.0.1");
                message = MessageFormat.format(message, statusCode, response);
                TasksAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message =
                    resourceBundle.getString("failed.to.get.all.tasks");
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
     * @return Whether deletion was successful.
     */
    public final boolean removeTask(final String id) {
        URI uri = UriBuilder.fromUri(tasksUri).path(id).build();

        TasksAPI.log.debug("DELETE {}", uri);
        Response response = getClient().target(uri).request()
                                       .header(HttpHeaders.AUTHORIZATION,
                                               getAuthorizationToken())
                                       .delete();

        return checkResponseGeneral(response);
    }

    /**
     * Set Runtime Data of a given task using a PATCH request.
     *
     * @param taskId           An id of a task.
     * @param patchRuntimeData A list of key-value pairs of data to be set.
     * @return Whether request was successful
     */
    public final boolean patchRuntimeData(
            final String taskId, final PatchRuntimeData patchRuntimeData) {
        URI uri = UriBuilder.fromUri(tasksUri).path(taskId).build();
        TasksAPI.log.debug("PATCH {}", uri);
        Entity<?> entity = Entity.entity(patchRuntimeData,
                                         MediaType.APPLICATION_JSON_TYPE);
        Response response =
                getClient().target(uri).request().method("PATCH", entity);
        return checkResponseGeneral(response);
    }

    private boolean checkResponseGeneral(final Response response) {
        try {
            Response.StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.log.debug(RootAPI.STATUS, statusCode, reasonPhrase);
            return status.getFamily() == Response.Status.Family.SUCCESSFUL;
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
