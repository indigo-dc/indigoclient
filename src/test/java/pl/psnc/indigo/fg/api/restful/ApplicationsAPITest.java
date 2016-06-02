package pl.psnc.indigo.fg.api.restful;

import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;

/**
 * Created by tzok on 20.05.16.
 */
public class ApplicationsAPITest {
    @Test
    public void testGetAllApplications() throws FutureGatewayException {
        ApplicationsAPI api = new ApplicationsAPI(BaseAPI.LOCALHOST_ADDRESS);
        api.getAllApplications();
    }
}
