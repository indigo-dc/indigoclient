package pl.psnc.indigo.cli.commands;

public AbstractICCommand {

  /* execute command can return
     - == 0 - everything is OK
     - != 0 - something is definitelly not right

    In case of super nasty issue, it may throw Exception
  */  
  public int execute() thows Exception;
}
