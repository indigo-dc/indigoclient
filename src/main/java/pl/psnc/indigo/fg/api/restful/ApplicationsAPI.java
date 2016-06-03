package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class ApplicationsAPI extends BaseAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationsAPI.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final Client client = ClientBuilder.newClient();
    private final String applicationsAddress;

    public ApplicationsAPI(String httpAddress) throws FutureGatewayException {
        super(httpAddress);
        applicationsAddress = RootAPI.getRootForAddress(httpAddress).getURLAsString() + "applications";
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public List<Application> getAllApplications() throws FutureGatewayException {
        String httpToCall = applicationsAddress;
        Response response = null;

        try {
            LOGGER.debug("GET " + httpToCall);
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {access_token}")
                    .get();

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status.getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.trace("Body: " + body);
                JsonNode jsonNode = mapper.readTree(body);
                JsonNode applications = jsonNode.get("applications");
                return Arrays.asList(mapper.treeToValue(applications, Application[].class));
            } else {
                String message = "Failed to list all applications. Response: " + response.getStatus() + " " + response;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to list all applications";
            LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public Application getApplication(Application application) throws FutureGatewayException {
        String httpToCall = applicationsAddress + "/" + application.getId();
        Response response = null;

        try {
            LOGGER.debug("GET " + httpToCall);
            response = client.target(httpToCall)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {access_token}")
                    .get();

            Response.StatusType status = response.getStatusInfo();
            LOGGER.debug("Status: " + status.getStatusCode() + " " + status.getReasonPhrase());

            if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
                String body = response.readEntity(String.class);
                LOGGER.trace("Body: " + body);
                return mapper.readValue(body, Application.class);
            } else {
                String message = "Failed to list application " + application.getId() + ". Response: " + response.getStatus() + " " + response;
                LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (IOException e) {
            String message = "Failed to list application" + application.getId();
            LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
