package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Allows to query Future Gateway about available applications.
 */
public class ApplicationsAPI extends RootAPI {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ApplicationsAPI.class);
    private static final String APPLICATIONS = "applications";

    private final URI applicationsUri;
    private final ResourceBundle resourceBundle =
            ResourceBundle.getBundle("messages"); //NON-NLS

    /**
     * Construct an instance configured to communicate with given Future Gateway
     * instance.
     *
     * @param baseUri            URI (protocol://host:port) of a Future Gateway
     *                           instance
     * @param authorizationToken Token which identifies the user to services.
     * @throws FutureGatewayException If communication with Future Gateway
     *                                instance fails.
     */
    public ApplicationsAPI(final URI baseUri, final String authorizationToken)
            throws FutureGatewayException {
        super(baseUri, authorizationToken);

        URI rootUri = getRootUri();
        applicationsUri =
                UriBuilder.fromUri(rootUri).path(ApplicationsAPI.APPLICATIONS)
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
        try {
            ApplicationsAPI.LOGGER.debug("GET {}", applicationsUri);
            HttpResponse response = Request.Get(applicationsUri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .execute().returnResponse();
            StatusLine statusLine = response.getStatusLine();
            ApplicationsAPI.LOGGER
                    .debug(RootAPI.STATUS, statusLine.getStatusCode(),
                           statusLine.getReasonPhrase());

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                try (ByteArrayOutputStream outputStream = new
                        ByteArrayOutputStream()) {
                    response.getEntity().writeTo(outputStream);
                    String body =
                            outputStream.toString(Charset.defaultCharset());
                    ApplicationsAPI.LOGGER.debug("Body: {}", body);
                    JsonNode jsonNode = getMapper().readTree(body);
                    jsonNode = jsonNode.get(ApplicationsAPI.APPLICATIONS);
                    Application[] applications = getMapper()
                            .treeToValue(jsonNode, Application[].class);
                    return Arrays.asList(applications);
                }
            } else {
                String message = resourceBundle.getString(
                        "failed.to.list.all.applications.response.0.1");
                message = MessageFormat
                        .format(message, statusLine.getStatusCode(), response);
                ApplicationsAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message =
                    resourceBundle.getString("failed.to.list.all.applications");
            ApplicationsAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
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
        try {
            URI uri = UriBuilder.fromUri(applicationsUri).path(id).build();
            ApplicationsAPI.LOGGER.debug("GET {}", uri);

            HttpResponse response = Request.Get(uri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      getAuthorizationToken())
                                           .execute().returnResponse();

            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            ApplicationsAPI.LOGGER
                    .debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                try (ByteArrayOutputStream outputStream = new
                        ByteArrayOutputStream()) {
                    response.getEntity().writeTo(outputStream);
                    String body =
                            outputStream.toString(Charset.defaultCharset());
                    ApplicationsAPI.LOGGER.trace("Body: {}", body);
                    return getMapper().readValue(body, Application.class);
                }
            } else {
                String message = resourceBundle
                        .getString("failed.to.list.application.0.response.1.2");
                message =
                        MessageFormat.format(message, id, statusCode, response);
                ApplicationsAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message =
                    resourceBundle.getString("failed.to.list.application.0");
            message = MessageFormat.format(message, id);
            ApplicationsAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        }
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this)
                .append("applicationsUri", applicationsUri).toString();
    }
}
