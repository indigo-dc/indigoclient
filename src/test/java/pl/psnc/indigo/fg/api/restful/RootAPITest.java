package pl.psnc.indigo.fg.api.restful;

import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;

import java.net.URISyntaxException;
import java.util.logging.Logger;

public class RootAPITest {
    private final static Logger LOGGER = Logger.getLogger(RootAPITest.class.getName());

    @Test
    public void testGetRoot() throws FutureGatewayException, URISyntaxException {
        RootAPI api = RootAPI.getRootForAddress(BaseAPI.LOCALHOST_ADDRESS);
        LOGGER.info("Result: " + api.rootUri);
    }
}
