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
   * This variable is used for setting size of File array.
   * It's always one as we always pass only one file at a time.
   */
  public static final int SIZE_OF_FILE_ARRAY = 1;

  /**
   * This command lists all available applications that are ready
   * to use at given FutureGateway server.
   *
   * @param id ID of the Task
   * @param fileName name of the file to be uploaded
   * @param filePath location of the file in local file system
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

    // we need table for uploadFileForTask
    // And we have just one, that's why we have magic number here
    File[] files = new File[SIZE_OF_FILE_ARRAY];
    files[0] = new File(filePath);

    final Upload result =
                    restAPI.uploadFileForTask(task, files);

    return 0;
  }
}
