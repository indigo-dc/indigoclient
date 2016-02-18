package pl.psnc.indigo.fg.api.restful;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;

public class RootAPI extends BaseAPI {

  private final static Logger LOGGER = Logger.getLogger(RootAPI.class.getName());
  private Root wsRoot;
  private static HashMap<String, RootAPI> rootMap;

  private RootAPI(String httpAddress) {
    super(httpAddress);
    try {
      wsRoot = this.getRoot();
    } catch (Exception ex) {
      LOGGER.severe("Error while calling: " + httpAddress + " to get Root description");
    }
  }

  public static RootAPI getRootForAddress(String httpAddress) {
    if (rootMap == null) {
      rootMap = new HashMap<String, RootAPI>();
    }

    if (rootMap.containsKey(httpAddress)) {
      return rootMap.get(httpAddress);
    } else {
      RootAPI newRoot = new RootAPI(httpAddress);
      rootMap.put(httpAddress, newRoot);
      return newRoot;
    }
  }

  private Root getRoot() throws Exception {

    String httpToCall = httpAddress;
    LOGGER.info("Calling: " + httpToCall);

    Client client = null;
    Response response = null;
    try {
      client = ClientBuilder.newClient();
      response = client.target(httpToCall)
        .request(MediaType.TEXT_PLAIN_TYPE)
        .get();

      if (response.getStatus() == 200) {
        LOGGER.info("Response status: " + response.getStatus());
        MultivaluedMap<String, Object> headers = response.getHeaders();
        String body = response.readEntity(String.class);

        LOGGER.info("Body: " + body);

        ObjectMapper mapper = new ObjectMapper();
        Root root = mapper.readValue(body, Root.class);
        return root;

      } else {
        LOGGER.severe("Error while calling: " + httpAddress + " - status: " + response.getStatus());
        return null;
      }
    } catch (JsonParseException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (JsonMappingException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error while calling: " + httpToCall, ex);
    } finally {
      if (response != null) {
        response.close();
      }
      if (client != null) {
        client.close();
      }
    }
    return null;
  }

  public String getURLAsString() {
    return httpAddress + "/" + wsRoot.getVersions().get(0).getId() + "/";
  }

}
