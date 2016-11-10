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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Category(UnitTests.class)
public class ApplicationsAPITest {
    private MockRestSession session;
    private ApplicationsAPI api;

    @Before
    public final void before() throws IOException, FutureGatewayException {
        session = new MockRestSession();
        Client client = session.getClient();
        api = new ApplicationsAPI(MockRestSession.MOCK_ADDRESS, client, "");
    }

    @Test
    public final void testGetAllApplications() throws FutureGatewayException {
        List<Application> applications = api.getAllApplications();
        assertThat(2, is(applications.size()));

        Application application = applications.get(0);
        List<Infrastructure> infrastructures = application.getInfrastructures();
        List<Parameter> parameters = application.getParameters();
        assertThat(2, is(infrastructures.size()));
        assertThat(5, is(parameters.size()));

        Application application1 = applications.get(1);
        List<Infrastructure> infrastructures1 =
                application1.getInfrastructures();
        List<Parameter> parameters1 = application1.getParameters();
        assertThat(2, is(infrastructures1.size()));
        assertThat(5, is(parameters1.size()));
    }

    /* This test exceptionally needs to ad-hoc add new response to the mock. */
    @Test(expected = FutureGatewayException.class)
    public final void testGetAllApplicationsError()
            throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                            .path("applications").build();
        session.mockGetPostResponse(uri, Response.Status.NOT_FOUND, "");
        api.getAllApplications();
    }

    @Test
    public final void testGetApplication() throws FutureGatewayException {
        Application application = api.getApplication("1");
        assertThat("1", is(application.getId()));
        assertThat("hostname", is(application.getName()));
        assertThat("hostname tester application",
                   is(application.getDescription()));
        assertThat(application.isEnabled(), is(true));

        List<Infrastructure> infrastructures = application.getInfrastructures();
        assertThat(2, is(infrastructures.size()));

        Infrastructure infrastructure = infrastructures.get(0);
        assertThat("1", is(infrastructure.getId()));
        assertThat("hello@csgfsdk", is(infrastructure.getName()));
        assertThat("hostname application localhost (SSH)",
                   is(infrastructure.getDescription()));
        assertThat(infrastructure.isEnabled(), is(true));
        assertThat(infrastructure.isVirtual(), is(false));

        List<Parameter> infrastructureParameters =
                infrastructure.getParameters();
        assertThat(3, is(infrastructureParameters.size()));

        Parameter infrastructureParameter = infrastructureParameters.get(0);
        assertThat("jobservice", is(infrastructureParameter.getName()));
        assertThat("ssh://localhost:22",
                   is(infrastructureParameter.getValue()));

        List<Parameter> applicationParameters = application.getParameters();
        assertThat(5, is(applicationParameters.size()));

        Parameter applicationParameter = applicationParameters.get(0);
        assertThat("jobdesc_executable", is(applicationParameter.getName()));
        assertThat("/bin/hostname", is(applicationParameter.getValue()));
        assertThat("", is(applicationParameter.getDescription()));
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetApplicationInvalidUri()
            throws FutureGatewayException {
        api.getApplication("invalid-uri");
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetApplicationInvalidBody()
            throws FutureGatewayException {
        api.getApplication("invalid-body");
    }
}
