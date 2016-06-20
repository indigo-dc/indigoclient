package pl.psnc.indigo.fg.api.restful;

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
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TasksAPI extends RootAPI {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TasksAPI.class);

    private final URI tasksUri;

    public TasksAPI(final String baseUri) throws FutureGatewayException,
            URISyntaxException {
        super(baseUri);

        tasksUri = rootUriBuilder().path("tasks").build();
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
    public final Task createTask(final Task task) throws
            FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", task
                .getUser()).build();
        Response response = null;

        try {
            LOGGER.debug("POST " + uri);
            response = getClient().target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType
                            .APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {access_token}")
                    .post(Entity.json(getMapper().writeValueAsString(task)));

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status
                    .getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.trace("Body: " + body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message = "Failed to create task. Response: "
                        + response.getStatus() + " " + response + "\nTask: "
                        + task;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to create task\nTask: " + task;
            LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Upload file for task
     */
    public final Upload uploadFileForTask(final Task task, final File file)
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri)
                .path(task.getId())
                .path("input")
                .queryParam("user", task.getUser())
                .build();
        FileDataBodyPart fileDataBodyPart = null;
        MultiPart multiPart = null;
        Response response = null;

        try {
            fileDataBodyPart = new FileDataBodyPart("file[]", file, MediaType
                    .APPLICATION_OCTET_STREAM_TYPE);

            multiPart = new MultiPart();
            multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
            multiPart.bodyPart(fileDataBodyPart);

            LOGGER.debug("POST " + uri);
            response = getClient().target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(multiPart, MediaType
                            .MULTIPART_FORM_DATA_TYPE));

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status
                    .getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.trace("Body: " + body);
                return getMapper().readValue(body, Upload.class);
            } else {
                String message = "Failed to upload file for task. Response: "
                        + response.getStatus() + " " + response + "\nTask: "
                        + task + "\nFile: " + file;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to upload file for task\nTask: " + task
                    + "\nFile: " + file;
            LOGGER.error(message, e);
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
        }
    }

    /**
     * Check status
     */
    public final Task getTask(final Task task) throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).path(task.getId()).build();
        Response response = null;

        try {
            LOGGER.debug("GET " + uri);
            response = getClient().target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType
                            .APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {access_token}")
                    .get();

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status
                    .getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.trace("Body: " + body);
                return getMapper().readValue(body, Task.class);
            } else {
                String message = "Failed to get task. Response: " + response
                        .getStatus() + " " + response + "\nTask: " + task;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get task\nTask: " + task;
            LOGGER.error(message, e);
            throw new FutureGatewayException(message);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Get output files for task
     */
    public final List<OutputFile> getOutputsForTask(final Task query)
            throws FutureGatewayException {
        Task task = getTask(query);

        // There is no sense to get output files for tasks that are not DONE
        // In case of tasks that are still running we will get the same url
        // that is: "url": "file?path=&name=sayhello.out"
        if (task.getStatus() != Task.Status.DONE
                || task.getOutputFiles() == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(task.getOutputFiles());
    }

    /**
     * Gets output file for the job
     */
    public final void downloadOutputFile(final OutputFile outputFile,
                                         final File directory)
            throws FutureGatewayException, URISyntaxException {
        URI outputFileUri = new URI(outputFile.getUrl());
        URI uri = rootUriBuilder().path(outputFileUri.getPath())
                .replaceQuery(outputFileUri.getQuery()).build();
        Response response = null;

        try {
            LOGGER.debug("GET " + uri);
            response = getClient().target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType
                            .APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {access_token}")
                    .get();

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status
                    .getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                InputStream is = response.readEntity(InputStream.class);
                Files.copy(is, new File(directory, outputFile.getName())
                        .toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                String message = "Failed to download file. Response: "
                        + response.getStatus() + " " + response + "\nOutput "
                        + "file: " + outputFile + "\nDirectory: " + directory;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to download file\nOutput file: "
                    + outputFile + "\nDirectory: " + directory;
            LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public final List<Task> getAllTasks(final String user)
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(tasksUri).queryParam("user", user).build();
        Response response = null;

        try {
            LOGGER.debug("GET " + uri);
            response = getClient().target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType
                            .APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {access_token}")
                    .get();

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status
                    .getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.trace("Body: " + body);
                return Arrays.asList(getMapper().readValue(body, Task[].class));
            } else {
                String message = "Failed to get all tasks. Response: "
                        + response.getStatus() + " " + response;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to get all tasks";
            LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public final boolean deleteTask(final Task task) {
        URI uri = UriBuilder.fromUri(tasksUri).path(task.getId()).build();
        Response response = null;

        try {
            LOGGER.debug("DELETE " + uri);
            response = getClient().target(uri)
                    .request()
                    .delete();

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status
                    .getReasonPhrase());
            return status.getFamily() == Response.Status.Family.SUCCESSFUL;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
