package pl.psnc.indigo.fg.api.restful;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Link;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;
import pl.psnc.indigo.fg.api.restful.jaxb.Version;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Category(UnitTests.class)
public class RootAPITest {
    private static final String URI_STRING = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public final void testGetRoot() throws Exception {
        stubFor(get(urlEqualTo("/")).willReturn(
                aResponse().withBody(Helper.readResource("root.json"))));

        RootAPI rootApi = new RootAPI(URI.create(RootAPITest.URI_STRING), "");

        URI expectedUri =
                UriBuilder.fromUri(RootAPITest.URI_STRING).path("v1.0").build();
        assertThat(expectedUri, is(rootApi.getRootUri()));

        Root root = rootApi.getRoot();

        List<Link> links = root.getLinks();
        assertThat(1, is(links.size()));
        Link link = links.get(0);
        assertThat("/", is(link.getHref()));
        assertThat("self", is(link.getRel()));

        List<Version> versions = root.getVersions();
        assertThat(1, is(versions.size()));
        Version version = versions.get(0);
        assertThat("prototype", is(version.getStatus()));
        assertThat("2016-04-20", is(version.getUpdated()));
        assertThat("v0.0.2-29-ge0d90af-e0d90af-34", is(version.getBuild()));
        assertThat(MediaType.APPLICATION_JSON_TYPE, is(version.getMediaType()));
        assertThat("v1.0", is(version.getId()));

        List<Link> versionLinks = version.getLinks();
        assertThat(1, is(versionLinks.size()));
        Link versionLink = versionLinks.get(0);
        assertThat("v1.0", is(versionLink.getHref()));
        assertThat("self", is(versionLink.getRel()));
    }


    @Test(expected = FutureGatewayException.class)
    public final void testCommunicationError() throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(RootAPITest.URI_STRING).path("invalid-uri")
                            .build();
        new RootAPI(uri, "");
    }

    @Test(expected = FutureGatewayException.class)
    public final void testInvalidJson() throws FutureGatewayException {
        URI uri =
                UriBuilder.fromUri(RootAPITest.URI_STRING).path("invalid-body")
                          .build();
        new RootAPI(uri, "");
    }
}
