package pl.psnc.indigo.fg.api.restful;

import java.net.URI;
import java.net.URISyntaxException;

public class BaseAPI {
    public static final String DEFAULT_ADDRESS = "http://90.147.74.77:8888";
    // In order to use localhost you have to:
    // either install FutureGateway at your machine
    // or - get ssh access to the machine and forward ports
    // ssh -o ServerAliveInterval=180 -i ssh.key -L 8080:localhost:8080 -L
    // 8888:localhost:8888 futuregateway@some_ip
    public static final String LOCALHOST_ADDRESS = "http://localhost:8888";

    private final URI baseUri;

    public BaseAPI(final String baseUri) throws URISyntaxException {
        this.baseUri = new URI(baseUri);
    }

    public final URI getBaseUri() {
        return baseUri;
    }
}
