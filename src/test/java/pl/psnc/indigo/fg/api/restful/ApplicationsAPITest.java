package pl.psnc.indigo.fg.api.restful;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.Parameter;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@Category(UnitTests.class)
public class ApplicationsAPITest {
    @Rule public WireMockRule wireMockRule = new WireMockRule();

    private ApplicationsAPI api;

    @Before
    public final void setup() throws IOException, FutureGatewayException {
        stubFor(get(urlEqualTo("/")).willReturn(
                aResponse().withBody(Helper.readResource("root.json"))));
        api = new ApplicationsAPI(URI.create("http://localhost:8080/"), "");
    }

    @Test
    public final void testGetAllApplications() throws Exception {
        final String body = Helper.readResource("applications.json");
        stubFor(get(urlEqualTo("/v1.0/applications"))
                        .willReturn(aResponse().withBody(body)));

        final List<Application> applications = api.getAllApplications();
        assertEquals(5, applications.size());

        final Application hostname = applications.get(1);
        final List<Integer> infrastructures = hostname.getInfrastructures();
        final List<Parameter> parameters = hostname.getParameters();
        assertEquals(2, infrastructures.size());
        assertEquals(5, parameters.size());

        final Application sayHello = applications.get(2);
        final List<Integer> infrastructures1 = sayHello.getInfrastructures();
        final List<Parameter> parameters1 = sayHello.getParameters();
        assertEquals(2, infrastructures1.size());
        assertEquals(5, parameters1.size());
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetAllApplicationsError()
            throws FutureGatewayException {
        stubFor(get(urlEqualTo("/v1.0/applications"))
                        .willReturn(aResponse().withBody("")));
        api.getAllApplications();
    }

    @Test
    public final void testGetApplication() throws Exception {
        final String body = Helper.readResource("applications_1.json");
        stubFor(get(urlEqualTo("/v1.0/applications/1"))
                        .willReturn(aResponse().withBody(body)));

        final Application application = api.getApplication("1");
        assertEquals("1", application.getId());
        assertEquals("hostname", application.getName());
        assertEquals("hostname tester application",
                     application.getDescription());
        assertTrue(application.isEnabled());

        final List<Integer> infrastructures = application.getInfrastructures();
        assertEquals(2, infrastructures.size());

        final List<Parameter> applicationParameters =
                application.getParameters();
        assertEquals(5, applicationParameters.size());

        final Parameter applicationParameter = applicationParameters.get(0);
        assertEquals("jobdesc_executable", applicationParameter.getName());
        assertEquals("/bin/hostname", applicationParameter.getValue());
        assertNull(applicationParameter.getDescription());
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetApplicationInvalidUri()
            throws FutureGatewayException {
        stubFor(get(urlEqualTo("/v1.0/applications/invalid-uri")).willReturn(
                aResponse().withStatus(HttpStatus.SC_NOT_FOUND)));

        api.getApplication("invalid-uri");
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetApplicationInvalidBody()
            throws FutureGatewayException {
        stubFor(get(urlEqualTo("/v1.0/applications/invalid-body"))
                        .willReturn(aResponse().withBody("")));
        api.getApplication("invalid-body");
    }
}
