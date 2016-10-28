package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Allows to query Future Gateway about available applications.
 */
@Slf4j
public class ApplicationsAPI extends RootAPI {
    private final URI applicationsUri;

    /**
     * Construct an instance configured to communicate with given Future Gateway
     * instance using non-default {@link Client}.
     *
     * @param baseUri            URI (protocol://host:port) of a Future Gateway
     *                           instance
     * @param client             Implementation of REST client.
     * @param authorizationToken Token which identifies the user to services.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                instance fails.
     */
    public ApplicationsAPI(final URI baseUri, final Client client,
                           final String authorizationToken)
            throws FutureGatewayException {
        super(baseUri, client, authorizationToken);

        URI rootUri = getRootUri();
        applicationsUri =
                UriBuilder.fromUri(rootUri).path("applications").build();
    }

    /**
     * Construct an instance configured to communicate with given Future Gateway
     * instance.
     *
     * @param baseUri            URI (protocol://host:port) of a Future Gateway
     *                           instance
     * @param authorizationToken Token which identifies the user to services.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                instance fails.
     */
    public ApplicationsAPI(final URI baseUri, final String authorizationToken)
            throws FutureGatewayException {
        super(baseUri, authorizationToken);

        URI rootUri = getRootUri();
        applicationsUri =
                UriBuilder.fromUri(rootUri).path("applications").build();
    }

    /**
     * Query Future Gateway about all applications currently available.
     *
     * @return A list of bean objects describing the applications.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final List<Application> getAllApplications()
            throws FutureGatewayException {
        ApplicationsAPI.log.debug("GET {}", applicationsUri);
        Response response = getClient().target(applicationsUri)
                                       .request(MediaType.APPLICATION_JSON_TYPE)
                                       .accept(MediaType.APPLICATION_JSON_TYPE)
                                       .header(HttpHeaders.CONTENT_TYPE,
                                               MediaType.APPLICATION_JSON)
                                       .header(HttpHeaders.AUTHORIZATION,
                                               getAuthorizationToken()).get();

        Response.StatusType status = response.getStatusInfo();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();
        ApplicationsAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                ApplicationsAPI.log.trace("Body: {}", body);
                JsonNode jsonNode = getMapper().readTree(body);
                jsonNode = jsonNode.get("applications");
                return Arrays.asList(getMapper().treeToValue(jsonNode,
                                                             Application[]
                                                                     .class));
            } else {
                String message =
                        "Failed to list all applications. Response: " + response
                                .getStatus() + ' ' + response;
                ApplicationsAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to list all applications";
            ApplicationsAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            response.close();
        }
    }

    /**
     * Get details about a single applications.
     *
     * @param id An id of the application to be checked.
     *
     * @return A bean object with details about application.
     *
     * @throws FutureGatewayException If communication with Future Gateway
     *                                fails.
     */
    public final Application getApplication(final String id)
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(applicationsUri).path(id).build();
        ApplicationsAPI.log.debug("GET {}", uri);
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
        ApplicationsAPI.log.debug("Status: {} {}", statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                ApplicationsAPI.log.trace("Body: {}", body);
                return getMapper().readValue(body, Application.class);
            } else {
                String message =
                        "Failed to list application " + id + ". Response: "
                        + response.getStatus() + ' ' + response;
                ApplicationsAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to list application " + id;
            ApplicationsAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            response.close();
        }
    }
}
