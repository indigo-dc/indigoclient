package pl.psnc.indigo.cli.commands;

import java.io.File;
import java.net.URI;
import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

/**
 * Class that performs listing of all aplications available in FG instance.
 */
public class UploadFileCommand implements AbstractCommand {

  private String   token;
  private String   url;
  private String   id;
  private String   fileName;
  private String   filePath;
  
  /**
   * This command lists all available applications that are ready
   * to use at given FutureGateway server.
   *
   * @param url URL of FG API server
   * @param token User's access token
   */
  public UploadFileCommand(
          final String url, 
          final String token,
          final String id,
          final String fileName,
          final String filePath) {
    this.url = url;
    this.token = token;
    this.id = id;
    this.fileName = fileName;
    this.filePath = filePath;
  }

  /**
   * Execute command.
   * @return 0    - everything is OK
   *         != 0 - something is definitely not right
   *
   * @throws Exception In case of super nasty issue, it may throw Exception
  */
  public final int execute() throws Exception {
    // we have to call stuff
    final Task task = new Task();
    task.setId(id);
    
    final TasksAPI restAPI = new TasksAPI(URI.create(url), token);
    
    File [] files = new File[1];
    files[0] = new File(filePath);
    
    final Upload result =
                    restAPI.uploadFileForTask(task, files);
    
    return 0;
  }
}
