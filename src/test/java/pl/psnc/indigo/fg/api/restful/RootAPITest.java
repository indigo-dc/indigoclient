package pl.psnc.indigo.fg.api.restful;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@Category(UnitTests.class)
public class RootAPITest {
    private static final String URI_STRING = "http://localhost:8080";

    @Rule public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public final void testGetRoot() throws Exception {
        stubFor(get(urlEqualTo("/")).willReturn(
                aResponse().withBody(Helper.readResource("root.json"))));

        RootAPI rootApi = new RootAPI(URI.create(RootAPITest.URI_STRING), "");

        URI expectedUri =
                UriBuilder.fromUri(RootAPITest.URI_STRING).path("v1.0").build();
        assertEquals(expectedUri, rootApi.getRootUri());

        Root root = rootApi.getRoot();

        List<Link> links = root.getLinks();
        assertEquals(1, links.size());
        Link link = links.get(0);
        assertEquals("/", link.getHref());
        assertEquals("self", link.getRel());

        List<Version> versions = root.getVersions();
        assertEquals(1, versions.size());
        Version version = versions.get(0);
        assertEquals("prototype", version.getStatus());
        assertEquals("2016-04-20", version.getUpdated());
        assertEquals("v0.0.2-29-ge0d90af-e0d90af-34", version.getBuild());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, version.getMediaType());
        assertEquals("v1.0", version.getId());

        List<Link> versionLinks = version.getLinks();
        assertEquals(1, versionLinks.size());
        Link versionLink = versionLinks.get(0);
        assertEquals("v1.0", versionLink.getHref());
        assertEquals("self", versionLink.getRel());
    }


    @Test(expected = FutureGatewayException.class)
    public final void testCommunicationError() throws FutureGatewayException {
        stubFor(get(urlEqualTo("/invalid-uri")).willReturn(
                aResponse().withStatus(HttpStatus.SC_NOT_FOUND)));

        URI uri = UriBuilder.fromUri(RootAPITest.URI_STRING).path("invalid-uri")
                            .build();
        new RootAPI(uri, "");
    }

    @Test(expected = FutureGatewayException.class)
    public final void testInvalidJson() throws FutureGatewayException {
        stubFor(get(urlEqualTo("/invalid-body"))
                        .willReturn(aResponse().withBody("")));

        URI uri =
                UriBuilder.fromUri(RootAPITest.URI_STRING).path("invalid-body")
                          .build();
        new RootAPI(uri, "");
    }
}
