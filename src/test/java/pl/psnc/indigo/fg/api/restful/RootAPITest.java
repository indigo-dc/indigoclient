package pl.psnc.indigo.fg.api.restful;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Link;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;
import pl.psnc.indigo.fg.api.restful.jaxb.Version;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("NestedMethodCall")
@Category(UnitTests.class)
public class RootAPITest {
    private MockRestSession session;

    @Before
    public void before() throws IOException {
        session = new MockRestSession();
    }

    @Test
    public void testGetRoot()
            throws FutureGatewayException, URISyntaxException {
        Client client = session.getClient();
        RootAPI rootApi = new RootAPI(MockRestSession.MOCK_ADDRESS, client);

        URI expectedUri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS)
                                    .path("v1.0").build();
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

        links = version.getLinks();
        assertEquals(1, links.size());
        link = links.get(0);
        assertEquals("v1.0", link.getHref());
        assertEquals("self", link.getRel());
    }

    @Test(expected = FutureGatewayException.class)
    public void testCommunicationError() throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS)
                            .path("invalid-uri").build();
        Client client = session.getClient();
        new RootAPI(uri, client);
    }

    @Test(expected = FutureGatewayException.class)
    public void testInvalidJson() throws FutureGatewayException {
        URI uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS)
                            .path("invalid-body").build();
        Client client = session.getClient();
        new RootAPI(uri, client);
    }
}
