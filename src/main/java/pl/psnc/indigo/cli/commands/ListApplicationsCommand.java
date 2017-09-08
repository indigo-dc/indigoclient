package pl.psnc.indigo.cli.commands;

public ListApplicationsCommand implements AbstractICCommand {

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
  ListApplicationsCommand(String url, String token) {
    this.url 			= url;
		this.token		= token;
  }

  ListApplicationsCommand(String url, String token, long timeout) {
		ListApplicationsCommand(url, token);

		this.timeout = timeout;
  }

  
  /* execute command can return
     - == 0 - everything is OK
     - != 0 - something is definitelly not right

    In case of super nasty issue, it may throw Exception
  */  
  public int execute() thows Exception {
    // we have to call stuff
  }
}
