package pl.psnc.indigo.fg.api.restful;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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
            } catch(Exception ex) {
                LOGGER.severe("Error while calling: " + httpAddress + " to get Root description");
            }
	}
        
        public static RootAPI getRootForAddress(String httpAddress) {
            if(rootMap == null) {
                rootMap = new HashMap<String, RootAPI>();
            }
            
            if(rootMap.containsKey(httpAddress)) {
                return rootMap.get(httpAddress);
            } else {
                RootAPI newRoot = new RootAPI(httpAddress);
                rootMap.put(httpAddress, newRoot);
                return newRoot;
            }
        }

	private Root getRoot() throws Exception {

		Client client = ClientBuilder.newClient();
		Response response = client.target(httpAddress)
		  .request(MediaType.TEXT_PLAIN_TYPE)
		  .get();

                if(response.getStatus() == 200) {
                    LOGGER.info("Response status: " + response.getStatus());
                    MultivaluedMap<String,Object> headers = response.getHeaders();
                    String body = response.readEntity(String.class);

                    LOGGER.info("Body: " + body);
                    
                    try { 
                            ObjectMapper mapper = new ObjectMapper();
                            Root root = mapper.readValue(body, Root.class);
                            return root;
                    } catch(Exception ex) {
                        LOGGER.log(Level.SEVERE, "Exception while parsing JSON value:", ex);
                        throw ex;
                    }  
                } else {
                    LOGGER.severe("Error while calling: " + httpAddress + " - status: " + response.getStatus());
                    return null;
                }
	}
        
        public String getURLAsString() {
            return httpAddress + "/" + wsRoot.getVersions().get(0).getId() + "/";
        }
               
}

