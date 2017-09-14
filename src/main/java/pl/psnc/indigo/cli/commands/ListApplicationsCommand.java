package pl.psnc.indigo.cli.commands;

import java.net.URI;
import java.util.List;
import pl.psnc.indigo.fg.api.restful.ApplicationsAPI;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;

/**
 * Class that performs listing of all aplications available in FG instance.
 */
public class ListApplicationsCommand implements AbstractCommand {

  private String   token;
  private String   url;
  private long     timeout;

  /**
   * This command lists all available applications that are ready
   * to use at given FutureGateway server.
   *
   * @param url URL of FG API server
   * @param token User's access token
   */
  public ListApplicationsCommand(final String url, final String token) {
    this.url = url;
    this.token = token;
  }

  /**
   * Constructor with parameters required to run command.
   *
   * @param url URL of FG API server
   * @param token User's access token
   * @param timeout Can be set to set timeout of the command
   */
  public ListApplicationsCommand(
    final String url,
    final String token,
    final long timeout) {

    this(url, token);

    this.timeout = timeout;
  }

  /**
   * Execute command.
   * @return 0    - everything is OK
   *         != 0 - something is definitelly not right
   *
   * @throws Exception In case of super nasty issue, it may throw Exception
  */
  public final int execute() throws Exception {
    // we have to call stuff
    ApplicationsAPI appapi = new ApplicationsAPI(URI.create(url), token);
    List<Application> list = appapi.getAllApplications();
    for (Application app : list) {
        System.out.println("ID: ["
                + app.getId()
                + "] - AppName: "
                + app.getName());
    }
    return 0;
  }
}
