package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tzok on 20.05.16.
 */
public class ApplicationsAPI extends BaseAPI {
    private static final Logger LOGGER = Logger.getLogger(ApplicationsAPI.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final String applicationsAddress;

    public ApplicationsAPI(String httpAddress) throws FutureGatewayException {
        super(httpAddress);
        applicationsAddress = RootAPI.getRootForAddress(httpAddress).getURLAsString() + "applications";
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public List<Application> getAllApplications() throws FutureGatewayException {
        String httpToCall = applicationsAddress;
        LOGGER.info("Calling: " + httpToCall);

        Client client = null;
        Response response = null;

        try {
            client = ClientBuilder.newClient();
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer {access_token}")
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            int status = response.getStatus();
            LOGGER.info("Response status: " + status);

            if (status == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.info("Body: " + body);
                JsonNode jsonNode = mapper.readTree(body);
                JsonNode applications = jsonNode.get("applications");
                return Arrays.asList(mapper.treeToValue(applications, Application[].class));
            } else {
                String message = "Failed to list all applications. Response: " + response.getStatus() + " " + response;
                LOGGER.severe(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to list all applications";
            LOGGER.log(Level.SEVERE, message, e);
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
}
