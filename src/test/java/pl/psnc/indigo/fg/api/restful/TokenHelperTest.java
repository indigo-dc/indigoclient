package pl.psnc.indigo.fg.api.restful;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@Category(UnitTests.class)
public class TokenHelperTest {
    @Rule public final WireMockRule wireMockRule = new WireMockRule();

    private final String validToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ" +
            "zdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4" +
            "gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cB" +
            "ab30RMHrHDcEfxjoYZgeFONFh7HgQ";
    private final String uriPath = "/api/jsonws/iam.token/get-token";
    private final URI tokenServiceUri =
            UriBuilder.fromUri("http://localhost:8080").path(uriPath).build();

    @Test
    public void testGetTokenValid() throws Exception {
        final String body = Helper.readResource("token-service.json");
        stubFor(post(urlEqualTo(uriPath)).willReturn(
                aResponse().withStatus(HttpStatus.SC_OK).withBody(body)));
        // FIXME: this sleep is needed because of a bug in Jetty (?) or WireMock
        // https://github.com/tomakehurst/wiremock/issues/97
        Thread.sleep(2000);
        final String token =
                TokenHelper.getToken(validToken, tokenServiceUri, "", "");
        assertEquals(validToken, token);
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTokenForbidden() throws Exception {
        stubFor(post(urlEqualTo(uriPath)).willReturn(
                aResponse().withStatus(HttpStatus.SC_FORBIDDEN)));
        TokenHelper.getToken(validToken, tokenServiceUri, "", "");
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTokenError() throws Exception {
        final String body = Helper.readResource("token-service-error.json");
        stubFor(post(urlEqualTo(uriPath)).willReturn(
                aResponse().withStatus(HttpStatus.SC_OK).withBody(body)));
        TokenHelper.getToken(validToken, tokenServiceUri, "", "");
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTokenInvalidOutputToken() throws Exception {
        final String body = Helper.readResource("token-service-invalid.json");
        stubFor(post(urlEqualTo(uriPath)).willReturn(
                aResponse().withStatus(HttpStatus.SC_OK).withBody(body)));
        TokenHelper.getToken(validToken, tokenServiceUri, "", "");
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTokenInvalidInputToken() throws Exception {
        TokenHelper.getToken("invalid", tokenServiceUri, "", "");
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTokenInvalidRequest() throws Exception {
        stubFor(post(urlEqualTo(uriPath)).willReturn(
                aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        TokenHelper.getToken(validToken, tokenServiceUri, "", "");
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTokenInvalidResponse() throws Exception {
        stubFor(post(urlEqualTo(uriPath)).willReturn(
                aResponse().withStatus(HttpStatus.SC_OK).withBody("")));
        TokenHelper.getToken(validToken, tokenServiceUri, "", "");
    }

}
