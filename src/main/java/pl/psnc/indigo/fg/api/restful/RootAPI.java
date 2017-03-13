package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * A base class which contains HTTP client and JSON mapper objects with
 * delegated methods for all subclasses.
 */
public class RootAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(RootAPI.class);

    /**
     * A localhost version of Future Gateway. Useful for tunnelled connections:
     * $ ssh -L 8080:localhost:8080 -L 8888:localhost:8888 futuregateway@IP
     */
    public static final URI LOCALHOST_ADDRESS =
            URI.create("http://localhost:8888"); //NON-NLS

    /** A constant used for all logging of REST communication. */
    static final String STATUS = "Status: {} {}";

    private final ResourceBundle resourceBundle =
            ResourceBundle.getBundle("messages"); //NON-NLS

    private final ObjectMapper mapper;
    private final URI rootUri;
    private final Root root;
    private final String authorizationToken;

    /**
     * Construct an instance for a given URI protocol://host:port. Refer to
     * constants DEFAULT_ADDRESS and LOCALHOST_ADDRESS.
     *
     * @param baseUri            URI of Future Gateway server.
     * @param authorizationToken Token which identifies the user to services.
     * @throws FutureGatewayException If failed to communicate with Future
     *                                Gateway.
     */
    RootAPI(final URI baseUri, final String authorizationToken)
            throws FutureGatewayException {
        super();

        String bearer = resourceBundle.getString("bearer.0");
        this.authorizationToken =
                MessageFormat.format(bearer, authorizationToken);

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new ISO8601DateFormat());

        root = getRootForUri(baseUri);
        String version = root.getVersions().get(0).getId();
        rootUri = UriBuilder.fromUri(baseUri).path(version).build();
    }

    final Root getRoot() {
        return root;
    }

    final URI getRootUri() {
        return rootUri;
    }

    protected final String getAuthorizationToken() {
        return authorizationToken;
    }

    private Root getRootForUri(final URI baseUri)
            throws FutureGatewayException {
        try {
            RootAPI.LOGGER.debug("GET {}", baseUri);

            HttpResponse response = Request.Get(baseUri)
                                           .setHeader(HttpHeaders.AUTHORIZATION,
                                                      authorizationToken)
                                           .execute().returnResponse();

            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            RootAPI.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                try (ByteArrayOutputStream outputStream = new
                        ByteArrayOutputStream()) {
                    response.getEntity().writeTo(outputStream);
                    String body =
                            outputStream.toString(Charset.defaultCharset());
                    RootAPI.LOGGER.trace("Body: {}", body);
                    return mapper.readValue(body, Root.class);
                }
            } else {
                String message = resourceBundle.getString(
                        "failed.to.connect.to.future.gateway.response.0.1");
                message = MessageFormat.format(message, statusCode, response);
                RootAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message = resourceBundle
                    .getString("failed.to.connect.to.future.gateway");
            RootAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        }
    }

    final ObjectMapper getMapper() {
        return mapper;
    }
}
