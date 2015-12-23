package pl.psnc.indigo.fg.api.restful.jaxb;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import pl.psnc.indigo.fg.api.restful.jaxb.*;

public class Task implements Serializable {
    
    String id;
    String date;
    String last_change;
    String application;
    String description;
    String status;
    String user;
    String creation;
    String iosandbox;
    List<String> arguments;
    List<InputFile> input_files;
    List<OutputFile> output_files;
    List<Links> _links;
    
    public Task() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLast_change() {
        return last_change;
    }

    public void setLast_change(String last_change) {
        this.last_change = last_change;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getIosandbox() {
        return iosandbox;
    }

    public void setIosandbox(String iosandbox) {
        this.iosandbox = iosandbox;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public List<InputFile> getInput_files() {
        return input_files;
    }

    public void setInput_files(List<InputFile> input_files) {
        this.input_files = input_files;
    }

    public List<OutputFile> getOutput_files() {
        return output_files;
    }

    public void setOutput_files(List<OutputFile> output_files) {
        this.output_files = output_files;
    }

    public List<Links> get_links() {
        return _links;
    }

    public void set_links(List<Links> _links) {
        this._links = _links;
    }
    
}
