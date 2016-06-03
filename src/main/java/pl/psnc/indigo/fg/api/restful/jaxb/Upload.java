package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing Upload description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Upload implements Serializable {
    private List<InputFile> files;
    private String message;
    private String task;
    private String status;

    public List<InputFile> getFiles() {
        return files;
    }

    public void setFiles(List<InputFile> files) {
        this.files = files;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "files=" + files +
                ", message='" + message + '\'' +
                ", task='" + task + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
