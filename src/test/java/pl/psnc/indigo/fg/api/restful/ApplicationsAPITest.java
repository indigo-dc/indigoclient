package pl.psnc.indigo.fg.api.restful;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import java.net.URISyntaxException;

/**
 * Created by tzok on 20.05.16.
 */
public class ApplicationsAPITest {
    private ApplicationsAPI api;

    @Before
    public void initialize() throws FutureGatewayException, URISyntaxException {
        api = new ApplicationsAPI(BaseAPI.LOCALHOST_ADDRESS);
    }

    @Test
    public void testGetAllApplications() throws FutureGatewayException {
        api.getAllApplications();
    }

    @Test
    public void testGetApplication() throws FutureGatewayException {
        Application application = new Application();
        application.setId("1");
        application = api.getApplication(application);
        Assert.assertEquals("hostname", application.getName());
    }
}
