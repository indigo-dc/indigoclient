package pl.psnc.indigo.fg.api.restful;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.Infrastructure;
import pl.psnc.indigo.fg.api.restful.jaxb.Parameter;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        stubFor(get(urlEqualTo("/v1.0/applications/invalid-uri")).willReturn(
                aResponse().withStatus(
                        Response.Status.NOT_FOUND.getStatusCode())));

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
