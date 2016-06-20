package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing Upload description
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Upload implements Serializable {
    private List<InputFile> files;
    private String message;
    private String task;
    private String status;

    public final List<InputFile> getFiles() {
        return files;
    }

    public final void setFiles(final List<InputFile> files) {
        this.files = files;
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

    public final String getTask() {
        return task;
    }

    public final void setTask(final String task) {
        this.task = task;
    }

    public final String getStatus() {
        return status;
    }

    public final void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public final String toString() {
        return "Upload{"
                + "files=" + files
                + ", message='" + message + '\''
                + ", task='" + task + '\''
                + ", status='" + status + '\''
                + '}';
    }
}
