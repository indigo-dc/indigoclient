package pl.psnc.indigo.fg.api.restful;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import static org.hamcrest.CoreMatchers.is;

public class ApplicationsAPITest {
    private ApplicationsAPI api;

    @Before
    public final void initialize() throws FutureGatewayException {
        api = new ApplicationsAPI(RootAPI.LOCALHOST_ADDRESS);
    }

    @Test
    public final void testGetAllApplications() throws FutureGatewayException {
        api.getAllApplications();
    }

    @Test
    public final void testGetApplication() throws FutureGatewayException {
        Application application = api.getApplication("1");
        String name = application.getName();
        Assert.assertThat("hostname", is(name));
    }
}
