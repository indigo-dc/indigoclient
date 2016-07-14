package pl.psnc.indigo.fg.api.restful;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.Infrastructure;
import pl.psnc.indigo.fg.api.restful.jaxb.Parameter;

import javax.ws.rs.client.Client;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(UnitTests.class)
public class ApplicationsAPITest {
    private MockRestSession session;
    private ApplicationsAPI api;

    @Before
    public void before() throws IOException, FutureGatewayException {
        session = new MockRestSession();
        Client client = session.getClient();
        api = new ApplicationsAPI(MockRestSession.MOCK_ADDRESS, client);
    }

    @Test
    public final void testGetAllApplications() throws FutureGatewayException {
        List<Application> applications = api.getAllApplications();
        assertEquals(2, applications.size());

        Application application = applications.get(0);
        List<Infrastructure> infrastructures = application.getInfrastructures();
        List<Parameter> parameters = application.getParameters();
        assertEquals(2, infrastructures.size());
        assertEquals(5, parameters.size());

        application = applications.get(1);
        infrastructures = application.getInfrastructures();
        parameters = application.getParameters();
        assertEquals(2, infrastructures.size());
        assertEquals(5, parameters.size());
    }

    @Test
    public final void testGetApplication() throws FutureGatewayException {
        Application application = api.getApplication("1");
        assertEquals("1", application.getId());
        assertEquals("hostname", application.getName());
        assertEquals("hostname tester application",
                     application.getDescription());
        assertTrue(application.isEnabled());

        List<Infrastructure> infrastructures = application.getInfrastructures();
        assertEquals(2, infrastructures.size());

        Infrastructure infrastructure = infrastructures.get(0);
        assertEquals("1", infrastructure.getId());
        assertEquals("hello@csgfsdk", infrastructure.getName());
        assertEquals("hostname application localhost (SSH)",
                     infrastructure.getDescription());
        assertTrue(infrastructure.isEnabled());
        assertFalse(infrastructure.isVirtual());

        List<Parameter> parameters = infrastructure.getParameters();
        assertEquals(3, parameters.size());

        Parameter parameter = parameters.get(0);
        assertEquals("jobservice", parameter.getName());
        assertEquals("ssh://localhost:22", parameter.getValue());

        parameters = application.getParameters();
        assertEquals(5, parameters.size());

        parameter = parameters.get(0);
        assertEquals("jobdesc_executable", parameter.getName());
        assertEquals("/bin/hostname", parameter.getValue());
        assertEquals("", parameter.getDescription());
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetApplicationInvalidUri() throws FutureGatewayException {
        api.getApplication("invalid-uri");
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetApplicationInvalidBody() throws FutureGatewayException {
        api.getApplication("invalid-body");
    }
}
