package pl.psnc.indigo.cli.commands;

public class ListApplicationsCommand implements AbstractCommand {

  String   token;
  String   url;
  long     timeout;

  /* This command lists all available applications that are ready
     to use at given FutureGateway server.

     It needs:
     - authorization token
     - FutureGateway API URL - address should point to properly configured
          FG API server
     - it may take timeout as additional parameter - in that case, after given
          time, timeout will be raised
  */
  public ListApplicationsCommand(String url, String token) {
    this.url    = url;
    this.token	= token;
  }

  public ListApplicationsCommand(String url, String token, long timeout) {
    this(url, token);

    this.timeout = timeout;
  }

  
  /* execute command can return
     - == 0 - everything is OK
     - != 0 - something is definitelly not right

    In case of super nasty issue, it may throw Exception
  */  
  public int execute() throws Exception {
    // we have to call stuff
    return 0;
  }
}
