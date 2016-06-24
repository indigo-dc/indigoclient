package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A base class which contains HTTP client and JSON mapper objects with
 * delegated methods for all subclasses.
 */
public class RootAPI {
    /**
     * A public IP of a production-level Future Gateway.
     */
    public static final URI DEFAULT_ADDRESS = URI
            .create("http://90.147.74.77:8888");

    /**
     * A localhost version of Future Gateway. Useful for tunnelled connections:
     * $ ssh -L 8080:localhost:8080 -L 8888:localhost:8888 futuregateway@IP
     */
    public static final URI LOCALHOST_ADDRESS = URI
            .create("http://localhost:8888");

    private static final Logger LOGGER = LoggerFactory.getLogger(RootAPI.class);

    private final Client client;
    private final ObjectMapper mapper;
    private final URI rootUri;
    private final Root root;

    /**
     * Construct an instance for a given URI protocol://host:port and with
     * non-default {@link Client}. Refer to
     * constants DEFAULT_ADDRESS and LOCALHOST_ADDRESS.
     *
     * @param baseUri URI of Future Gateway server.
     * @param client  Implementation of REST client.
     * @throws FutureGatewayException If failed to communicate with Future
     *                                Gateway.
     */
    protected RootAPI(final URI baseUri, final Client client)
            throws FutureGatewayException {
        this.client = client;

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setDateFormat(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US));

        root = getRootForUri(baseUri);
        String version = root.getVersions().get(0).getId();
        rootUri = UriBuilder.fromUri(baseUri).path(version).build();
    }

    /**
     * Construct an instance for a given URI protocol://host:port. Refer to
     * constants DEFAULT_ADDRESS and LOCALHOST_ADDRESS.
     *
     * @param baseUri URI of Future Gateway server.
     * @throws FutureGatewayException If failed to communicate with Future
     *                                Gateway.
     */
    protected RootAPI(final URI baseUri) throws FutureGatewayException {
        this(baseUri,
             ClientBuilder.newBuilder().register(MultiPartFeature.class)
                          .build());
    }

    public final Root getRoot() {
        return root;
    }

    public final URI getRootUri() {
        return rootUri;
    }

    private Root getRootForUri(final URI baseUri)
            throws FutureGatewayException {
        Response response = null;

        try {
            RootAPI.LOGGER.debug("GET {}", baseUri);
            response = client.target(baseUri).request(MediaType.TEXT_PLAIN_TYPE)
                             .get();

            StatusType status = response.getStatusInfo();
            int statusCode = status.getStatusCode();
            String reasonPhrase = status.getReasonPhrase();
            RootAPI.LOGGER.debug("Status: {} {}", statusCode, reasonPhrase);

            if (statusCode == Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                RootAPI.LOGGER.trace("Body: {}", body);
                return mapper.readValue(body, Root.class);
            } else {
                String message =
                        "Failed to connect to Future Gateway. Response: "
                        + response.getStatus() + ' ' + response;
                RootAPI.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to connect to Future Gateway";
            RootAPI.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public final Client getClient() {
        return client;
    }

    public final ObjectMapper getMapper() {
        return mapper;
    }
}
