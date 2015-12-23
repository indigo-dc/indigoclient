
package pl.psnc.indigo.fg.api.restful.jaxb;

import java.io.Serializable;
import java.util.List;


public class Upload implements Serializable {
    
    private List<InputFile> files;
    private String message;
    private String task;
    private String gestatus;
    
    public Upload() {
        
    }

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

    public String getGestatus() {
        return gestatus;
    }

    public void setGestatus(String gestatus) {
        this.gestatus = gestatus;
    }
    
    
    
}
