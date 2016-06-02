package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RootAPI extends BaseAPI {
    private static final Logger LOGGER = Logger.getLogger(RootAPI.class.getName());
    private static final Map<String, RootAPI> rootMap = new HashMap<>();

    public static RootAPI getRootForAddress(String httpAddress) throws FutureGatewayException {
        if (!rootMap.containsKey(httpAddress)) {
            rootMap.put(httpAddress, new RootAPI(httpAddress));
        }
        return rootMap.get(httpAddress);
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final Root wsRoot;

    private RootAPI(String httpAddress) throws FutureGatewayException {
        super(httpAddress);
        wsRoot = this.getRoot();
    }

    private Root getRoot() throws FutureGatewayException {
        String httpToCall = httpAddress;
        LOGGER.info("Calling: " + httpToCall);

        Client client = null;
        Response response = null;

        try {
            client = ClientBuilder.newClient();
            response = client.target(httpToCall)
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .get();
            int status = response.getStatus();
            LOGGER.info("Response status: " + status);

            if (status == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.info("Body: " + body);
                return mapper.readValue(body, Root.class);
            } else {
                String message = "Failed to connect to Future Gateway. Response: " + response.getStatus() + " " + response;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to connect to Future Gateway";
            LOGGER.severe(message);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    public String getURLAsString() {
        return httpAddress + "/" + wsRoot.getVersions().get(0).getId() + "/";
    }
}
