package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * A base class which contains HTTP client and JSON mapper objects with
 * delegated methods for all subclasses.
 */
@Slf4j
public class RootAPI {
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

    private final Client client;
    private final ObjectMapper mapper;
    private final URI rootUri;
    private final Root root;
    private final String authorizationToken;

    /**
     * Construct an instance for a given URI protocol://host:port and with
     * non-default {@link Client}. Refer to constants DEFAULT_ADDRESS and
     * LOCALHOST_ADDRESS.
     *
     * @param baseUri            URI of Future Gateway server.
     * @param client             Implementation of REST client.
     * @param authorizationToken Token which identifies the user to services.
     * @throws FutureGatewayException If failed to communicate with Future
     *                                Gateway.
     */
    RootAPI(
            final URI baseUri, final Client client,
            final String authorizationToken) throws FutureGatewayException {
        super();

        String bearer = resourceBundle.getString("bearer.0");

        this.client = client;
        this.authorizationToken =
                MessageFormat.format(bearer, authorizationToken);

        // If TRACE level is enabled, log whole requests, headers & body
        if (RootAPI.log.isTraceEnabled()) {
            client.register(new RequestLogger());
        }

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new ISO8601DateFormat());

        root = getRootForUri(baseUri);
        String version = root.getVersions().get(0).getId();
        rootUri = UriBuilder.fromUri(baseUri).path(version).build();
    }

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
        this(baseUri,
             ClientBuilder.newBuilder().register(MultiPartFeature.class)
                          .build(), authorizationToken);
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

        RootAPI.log.debug("GET {}", baseUri);
        Response response =
                client.target(baseUri).request(MediaType.TEXT_PLAIN_TYPE)
                      .header(HttpHeaders.AUTHORIZATION, authorizationToken)
                      .get();

        Response.StatusType status = response.getStatusInfo();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();
        RootAPI.log.debug(RootAPI.STATUS, statusCode, reasonPhrase);

        try {
            if (statusCode == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                RootAPI.log.trace("Body: {}", body);
                return mapper.readValue(body, Root.class);
            } else {
                String message = resourceBundle.getString(
                        "failed.to.connect.to.future.gateway.response.0.1");
                message = MessageFormat.format(message, statusCode, response);
                RootAPI.log.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            String message = resourceBundle
                    .getString("failed.to.connect.to.future.gateway");
            RootAPI.log.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            response.close();
        }
    }

    final Client getClient() {
        return client;
    }

    final ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("rootUri", rootUri).toString();
    }
}
