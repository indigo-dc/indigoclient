package pl.psnc.indigo.cli.commands;

import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import java.io.File;
import java.net.URI;

/**
 * Class that performs listing of all aplications available in FG instance.
 */
public class UploadFile implements AbstractCommand {
    private final String token;
    private final String url;
    private final String id;
    private final String filePath;

    /**
     * This command lists all available applications that are ready
     * to use at given FutureGateway server.
     *
     * @param id       ID of the Task
     * @param filePath location of the file in local file system
     * @param url      URL of FG API server
     * @param token    User's access token
     */
    public UploadFile(final String url, final String token, final String id,
                      final String filePath) {
        super();
        this.url = url;
        this.token = token;
        this.id = id;
        this.filePath = filePath;
    }

    /**
     * Execute command.
     *
     * @return 0    - everything is OK
     * != 0 - something is definitely not right
     * @throws FutureGatewayException When it was impossible to upload files.
     */
    public final int execute() throws FutureGatewayException {
        // we have to call stuff
        final Task task = new Task();
        task.setId(id);

        final TasksAPI api = new TasksAPI(URI.create(url), token);
        final Upload result = api.uploadFileForTask(task, new File(filePath));

        if (!"uploaded".equals(result.getMessage())) {
            return -1;
        }

        return 0;
    }
}
