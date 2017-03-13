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
import pl.psnc.indigo.fg.api.restful.jaxb.Infrastructure;
import pl.psnc.indigo.fg.api.restful.jaxb.Parameter;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(UnitTests.class)
public class ApplicationsAPITest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private ApplicationsAPI api;

    @Before
    public final void setup() throws IOException, FutureGatewayException {
        stubFor(get(urlEqualTo("/")).willReturn(
                aResponse().withBody(Helper.readResource("root.json"))));
        api = new ApplicationsAPI(URI.create("http://localhost:8080/"), "");
    }

    @Test
    public final void testGetAllApplications() throws Exception {
        String body = Helper.readResource("applications.json");
        stubFor(get(urlEqualTo("/v1.0/applications"))
                        .willReturn(aResponse().withBody(body)));

        List<Application> applications = api.getAllApplications();
        assertEquals(2, applications.size());

        Application application = applications.get(0);
        List<Infrastructure> infrastructures = application.getInfrastructures();
        List<Parameter> parameters = application.getParameters();
        assertEquals(2, infrastructures.size());
        assertEquals(5, parameters.size());

        Application application1 = applications.get(1);
        List<Infrastructure> infrastructures1 =
                application1.getInfrastructures();
        List<Parameter> parameters1 = application1.getParameters();
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
        String body = Helper.readResource("applications_1.json");
        stubFor(get(urlEqualTo("/v1.0/applications/1"))
                        .willReturn(aResponse().withBody(body)));

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

        List<Parameter> infrastructureParameters =
                infrastructure.getParameters();
        assertEquals(3, infrastructureParameters.size());

        Parameter infrastructureParameter = infrastructureParameters.get(0);
        assertEquals("jobservice", infrastructureParameter.getName());
        assertEquals("ssh://localhost:22", infrastructureParameter.getValue());

        List<Parameter> applicationParameters = application.getParameters();
        assertEquals(5, applicationParameters.size());

        Parameter applicationParameter = applicationParameters.get(0);
        assertEquals("jobdesc_executable", applicationParameter.getName());
        assertEquals("/bin/hostname", applicationParameter.getValue());
        assertEquals("", applicationParameter.getDescription());
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
