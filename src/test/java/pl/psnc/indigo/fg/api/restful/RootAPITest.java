package pl.psnc.indigo.fg.api.restful;

import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.RootAPI;
import pl.psnc.indigo.fg.api.restful.BaseAPI;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;

public class RootAPITest {

  private final static Logger LOGGER = Logger.getLogger(RootAPITest.class.getName());

  @Test
  public void testGetRoot() {
    RootAPI api = RootAPI.getRootForAddress(BaseAPI.LOCALHOST_ADDRESS);

    LOGGER.info("Result: " + api.getURLAsString());
  }

  @Test
  public void testFinalize() {

  }
}
