package pl.psnc.indigo.cli.commands;

import pl.psnc.indigo.fg.api.restful.ApplicationsAPI;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

import java.net.URI;
import java.util.List;

/**
 * Class that performs listing of all aplications available in FG instance.
 */
public class ListApplications implements AbstractCommand {
    private final String token;
    private final String url;

    /**
     * This command lists all available applications that are ready
     * to use at given FutureGateway server.
     *
     * @param url   URL of FG API server
     * @param token User's access token
     */
    public ListApplications(final String url, final String token) {
        super();
        this.url = url;
        this.token = token;
    }

    /**
     * Execute command.
     *
     * @return 0    - everything is OK
     * != 0 - something is definitelly not right
     * @throws FutureGatewayException When application listing fails.
     */
    public final int execute() throws FutureGatewayException {
        // we have to call stuff
        final ApplicationsAPI appapi =
                new ApplicationsAPI(URI.create(url), token);
        final List<Application> list = appapi.getAllApplications();
        for (final Application app : list) {
            System.out.println(
                    "ID: [" + app.getId() + "] - AppName: " + app.getName());
        }
        return 0;
    }
}
