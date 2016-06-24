package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Allows to query Future Gateway about available applications.
 */
public class ApplicationsAPI extends RootAPI {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ApplicationsAPI.class);

    private final URI applicationsUri;

    /**
     * Construct an instance configured to communicate with given Future
     * Gateway instance using non-default {@link Client}.
     *
     * @param baseUri URI (protocol://host:port) of a Future Gateway instance
     * @param client  Implementation of REST client.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                instance fails.
     */
    public ApplicationsAPI(final URI baseUri, final Client client)
            throws FutureGatewayException {
        super(baseUri, client);

        URI rootUri = getRootUri();
        applicationsUri = UriBuilder.fromUri(rootUri).path("applications")
                                    .build();
    }

    /**
     * Construct an instance configured to communicate with given Future
     * Gateway instance.
     *
     * @param baseUri URI (protocol://host:port) of a Future Gateway instance
     * @throws FutureGatewayException If communication with Future Gateway
     *                                instance fails.
     */
    public ApplicationsAPI(final URI baseUri) throws FutureGatewayException {
        super(baseUri);

        URI rootUri = getRootUri();
        applicationsUri = UriBuilder.fromUri(rootUri).path("applications")
                                    .build();
    }

    /**
     * Query Future Gateway about all applications currently available.
     *
     * @return A list of bean objects describing the applications.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final List<Application> getAllApplications()
            throws FutureGatewayException {
        Response response = null;

        try {
            ApplicationsAPI.LOGGER.debug("GET {}", applicationsUri);
            response = getClient().target(applicationsUri)
                                  .request(MediaType.APPLICATION_JSON_TYPE)
                                  .accept(MediaType.APPLICATION_JSON_TYPE)
                                  .header(HttpHeaders.CONTENT_TYPE,
                                          MediaType.APPLICATION_JSON)
                                  .header(HttpHeaders.AUTHORIZATION,
                                          "Bearer {access_token}").get();

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            ApplicationsAPI.LOGGER
                    .debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                ApplicationsAPI.LOGGER.trace("Body: {}", body);
                JsonNode jsonNode = getMapper().readTree(body);
                jsonNode = jsonNode.get("applications");
                Application[] applications = getMapper()
                        .treeToValue(jsonNode, Application[].class);
                return Arrays.asList(applications);
            } else {
                String message = "Failed to list all applications. Response: "
                                 + response.getStatus() + ' ' + response;
                ApplicationsAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to list all applications";
            ApplicationsAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Get details about a single applications.
     *
     * @param id An id of the application to be checked.
     * @return A bean object with details about application.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Application getApplication(final String id)
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(applicationsUri).path(id).build();
        Response response = null;

        try {
            ApplicationsAPI.LOGGER.debug("GET {}", uri);
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
            ApplicationsAPI.LOGGER
                    .debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                ApplicationsAPI.LOGGER.trace("Body: {}", body);
                return getMapper().readValue(body, Application.class);
            } else {
                String message = "Failed to list application " + id
                                 + ". Response: " + response.getStatus() + ' '
                                 + response;
                ApplicationsAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to list application " + id;
            ApplicationsAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}