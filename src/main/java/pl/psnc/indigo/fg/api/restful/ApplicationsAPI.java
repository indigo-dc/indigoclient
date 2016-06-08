package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ApplicationsAPI extends RootAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationsAPI.class);

    private final URI applicationsUri;

    public ApplicationsAPI(String baseUri) throws FutureGatewayException, URISyntaxException {
        super(baseUri);

        applicationsUri = UriBuilder.fromUri(rootUri).path("applications").build();
    }

    public List<Application> getAllApplications() throws FutureGatewayException {
        Response response = null;

        try {
            LOGGER.debug("GET " + applicationsUri);
            response = client.target(applicationsUri)
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
        URI uri = UriBuilder.fromUri(applicationsUri).path(application.getId()).build();
        Response response = null;

        try {
            LOGGER.debug("GET " + uri);
            response = client.target(uri)
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
