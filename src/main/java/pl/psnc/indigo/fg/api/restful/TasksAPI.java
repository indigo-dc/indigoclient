package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.PatchRuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Allows to manipulate tasks via Future Gateway: list, submit, delete.
 */
public class TasksAPI extends RootAPI {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TasksAPI.class);
    private static final String TASKS = "tasks";

    private final ResourceBundle resourceBundle =
            ResourceBundle.getBundle("messages"); //NON-NLS

    private final URI tasksUri;

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
        try {
            String taskJson = getMapper().writeValueAsString(task);
            HttpEntity entity =
                    new StringEntity(taskJson, ContentType.APPLICATION_JSON);

            String user = task.getUser();
            URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", user)
                                .build();
            TasksAPI.LOGGER.debug("POST {}", uri);

            HttpResponse response = Request.Post(uri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .body(entity).execute()
                                           .returnResponse();

            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                return readTask(response);
            } else {
                String message = resourceBundle
                        .getString("failed.to.create.task.response.0.1.task.2");
                message = MessageFormat
                        .format(message, statusCode, response, task);
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message =
                    resourceBundle.getString("failed.to.create.task.0");
            message = MessageFormat.format(message, task);
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        }
    }

    /**
     * Parse HTTP response to construct an instance of {@link Task}.
     *
     * @param response HTTP response to a REST call.
     * @return A {@link Task} object mapped from HTTP response.
     * @throws IOException          When I/O operation failed.
     * @throws JsonParseException   When the JSON in HTTP response is invalid.
     * @throws JsonMappingException When the JSON in HTTP response does not map
     *                              to {@link Task}.
     */
    private Task readTask(final HttpResponse response)
            throws IOException, JsonParseException, JsonMappingException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            response.getEntity().writeTo(outputStream);
            String body =
                    outputStream.toString(Charset.defaultCharset().name());
            TasksAPI.LOGGER.trace("Body: {}", body);
            return getMapper().readValue(body, Task.class);
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
        try {
            String id = task.getId();
            String user = task.getUser();
            URI uri = UriBuilder.fromUri(tasksUri).path(id).path("input")
                                .queryParam("user", user).build();
            TasksAPI.LOGGER.debug("POST {}", uri);

            HttpEntity httpEntity = MultipartEntityBuilder.create()
                                                          .addBinaryBody(
                                                                  "file[]",
                                                                  file).build();
            HttpResponse response = Request.Post(uri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .body(httpEntity).execute()
                                           .returnResponse();

            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                try (ByteArrayOutputStream outputStream = new
                        ByteArrayOutputStream()) {
                    response.getEntity().writeTo(outputStream);
                    String body = outputStream
                            .toString(Charset.defaultCharset().name());
                    TasksAPI.LOGGER.trace("Body: {}", body);
                    return getMapper().readValue(body, Upload.class);
                }
            } else {
                String message = resourceBundle.getString(
                        "failed.to.upload.file.for.task.response.0.1.task.2.3");
                message = MessageFormat
                        .format(message, statusCode, response, task, file);
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message = resourceBundle
                    .getString("failed.to.upload.file.for.task.0.file.1");
            message = MessageFormat.format(message, task, file);
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
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
        try {
            URI uri = UriBuilder.fromUri(tasksUri).path(id).build();
            TasksAPI.LOGGER.debug("GET {}", uri);

            HttpResponse response = Request.Get(uri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .execute().returnResponse();

            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                return readTask(response);
            } else {
                String message = resourceBundle
                        .getString("failed.to.get.task.response.0.1.task.2");
                message =
                        MessageFormat.format(message, statusCode, response, id);
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message = resourceBundle.getString("failed.to.get.task.0");
            message = MessageFormat.format(message, id);
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
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

        try {
            URI outputFileUri = outputFile.getUrl();
            String path = outputFileUri.getPath();
            String query = outputFileUri.getQuery();
            URI rootUri = getRootUri();
            URI uri = UriBuilder.fromUri(rootUri).path(path).replaceQuery(query)
                                .build();
            TasksAPI.LOGGER.debug("GET {}", uri);

            HttpResponse response = Request.Get(uri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .execute().returnResponse();

            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                try (ByteArrayOutputStream outputStream = new
                        ByteArrayOutputStream()) {
                    response.getEntity().writeTo(outputStream);
                    File file = new File(directory, outputFile.getName());
                    FileUtils.writeByteArrayToFile(file,
                                                   outputStream.toByteArray());
                }
            } else {
                String message = resourceBundle.getString(
                        "failed.to.download.file.response.0.1.output.file.2.3");
                message = MessageFormat
                        .format(message, statusCode, response, outputFile,
                                directory);
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            throw new FutureGatewayException("Failed to download file", e);
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
        try {
            URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", user)
                                .build();
            TasksAPI.LOGGER.debug("GET {}", uri);

            HttpResponse response = Request.Get(uri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .execute().returnResponse();

            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            TasksAPI.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                try (ByteArrayOutputStream outputStream = new
                        ByteArrayOutputStream()) {
                    response.getEntity().writeTo(outputStream);
                    String body = outputStream
                            .toString(Charset.defaultCharset().name());
                    TasksAPI.LOGGER.trace("Body: {}", body);
                    JsonNode jsonNode = getMapper().readTree(body);
                    jsonNode = jsonNode.get("tasks"); //NON-NLS
                    Task[] tasks =
                            getMapper().treeToValue(jsonNode, Task[].class);
                    return Arrays.asList(tasks);
                }
            } else {
                String message = resourceBundle
                        .getString("failed.to.get.all.tasks.response.0.1");
                message = MessageFormat.format(message, statusCode, response);
                TasksAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message =
                    resourceBundle.getString("failed.to.get.all.tasks");
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        }
    }

    /**
     * Delete a single task.
     *
     * @param id An id of a task.
     * @return Whether deletion was successful.
     * @throws FutureGatewayException When the operation fails.
     */
    public final boolean removeTask(final String id)
            throws FutureGatewayException {
        try {
            URI uri = UriBuilder.fromUri(tasksUri).path(id).build();
            TasksAPI.LOGGER.debug("DELETE {}", uri);

            HttpResponse response = Request.Delete(uri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .execute().returnResponse();
            return TasksAPI.checkResponseGeneral(response);
        } catch (final IOException e) {
            String message = MessageFormat
                    .format(resourceBundle.getString("failed.to.delete.task.0"),
                            id);
            TasksAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        }
    }

    /**
     * Set Runtime Data of a given task using a PATCH request.
     *
     * @param id               An id of a task.
     * @param patchRuntimeData A list of key-value pairs of data to be set.
     * @return Whether request was successful
     * @throws FutureGatewayException When the operation fails.
     */
    public final boolean patchRuntimeData(
            final String id, final PatchRuntimeData patchRuntimeData)
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).path(id).build();
        TasksAPI.LOGGER.debug("PATCH {}", uri);

        try {
            HttpEntity entity = new StringEntity(
                    getMapper().writeValueAsString(patchRuntimeData),
                    ContentType.APPLICATION_JSON);
            HttpResponse response =
                    Request.Patch(uri).body(entity).execute().returnResponse();
            return TasksAPI.checkResponseGeneral(response);
        } catch (final IOException e) {
            String message = MessageFormat.format(resourceBundle.getString(
                    "failed.to.patch.task.0.with.data.1"), id,
                                                  patchRuntimeData);
            throw new FutureGatewayException(message, e);
        }
    }

    /**
     * Check if HTTP response was successful.
     *
     * @param response An HTTP response to previous REST call.
     * @return True if the status of HTTP response belongs to the successful
     * family.
     */
    private static boolean checkResponseGeneral(final HttpResponse response) {
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();
        TasksAPI.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);
        return Response.Status.Family.familyOf(statusCode)
               == Response.Status.Family.SUCCESSFUL;
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this).append("tasksUri", tasksUri)
                                        .toString();
    }
}
