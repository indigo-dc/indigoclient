package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class RootAPI extends BaseAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(RootAPI.class);
    private static final Map<String, RootAPI> ROOT_API_MAP = new HashMap<>();

    public static RootAPI getRootForAddress(final String httpAddress) throws
            FutureGatewayException, URISyntaxException {
        if (!ROOT_API_MAP.containsKey(httpAddress)) {
            ROOT_API_MAP.put(httpAddress, new RootAPI(httpAddress));
        }
        return ROOT_API_MAP.get(httpAddress);
    }

    private final URI rootUri;
    private final Client client = ClientBuilder.newBuilder()
            .register(MultiPartFeature.class)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    protected RootAPI(final String baseUri) throws FutureGatewayException,
            URISyntaxException {
        super(baseUri);

        Root wsRoot = getRoot();
        String version = wsRoot.getVersions().get(0).getId();
        rootUri = UriBuilder.fromUri(baseUri)
                .path(version)
                .build();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    protected final Client getClient() {
        return client;
    }

    protected final ObjectMapper getMapper() {
        return mapper;
    }

    private Root getRoot() throws FutureGatewayException {
        Response response = null;

        try {
            LOGGER.debug("GET " + getBaseUri());
            response = client.target(getBaseUri())
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .get();

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status
                    .getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.trace("Body: " + body);
                return mapper.readValue(body, Root.class);
            } else {
                String message = "Failed to connect to Future Gateway. "
                        + "Response: " + response.getStatus() + " " + response;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to connect to Future Gateway";
            LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    protected final UriBuilder rootUriBuilder() {
        return UriBuilder.fromUri(rootUri);
    }
}
