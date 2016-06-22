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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A base class which contains HTTP client and JSON mapper objects with
 * delegated methods for all subclasses.
 */
public class RootAPI {
    /**
     * A public IP of a production-level Future Gateway.
     */
    public static final String DEFAULT_ADDRESS = "http://90.147.74.77:8888";
    /**
     * A localhost version of Future Gateway. Useful for tunnelled connections:
     * $ ssh -L 8080:localhost:8080 -L 8888:localhost:8888 futuregateway@IP
     */
    public static final String LOCALHOST_ADDRESS = "http://localhost:8888";

    private static final Logger LOGGER = LoggerFactory.getLogger(RootAPI.class);
    private static final Map<String, RootAPI> ROOT_API_MAP = new HashMap<>(1);

    static RootAPI getRootForAddress(final String httpAddress)
            throws FutureGatewayException {
        if (!RootAPI.ROOT_API_MAP.containsKey(httpAddress)) {
            RootAPI.ROOT_API_MAP.put(httpAddress, new RootAPI(httpAddress));
        }
        return RootAPI.ROOT_API_MAP.get(httpAddress);
    }

    private final Client client = ClientBuilder.newBuilder()
                                               .register(MultiPartFeature.class)
                                               .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final URI rootUri;

    /**
     * Construct an instance for a given URI protocol://host:port. Refer to
     * constants DEFAULT_ADDRESS and LOCALHOST_ADDRESS.
     *
     * @param uriString URI of Future Gateway server.
     * @throws FutureGatewayException If failed to communicate with Future
     *                                Gateway.
     */
    protected RootAPI(final String uriString) throws FutureGatewayException {
        URI baseUri = UriBuilder.fromUri(uriString).build();
        Root wsRoot = getRoot(baseUri);
        String version = wsRoot.getVersions().get(0).getId();
        rootUri = UriBuilder.fromUri(uriString).path(version).build();

        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setDateFormat(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US));
    }

    public final URI getRootUri() {
        return rootUri;
    }

    private Root getRoot(final URI baseUri) throws FutureGatewayException {
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
