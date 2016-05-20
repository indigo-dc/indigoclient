package pl.psnc.indigo.fg.api.restful.jaxb;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing Task description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task implements Serializable {
    private String id;
    private String date;
    private String lastChange;
    private String application;
    private String description;
    private String status;
    private String user;
    private String creation;
    private String iosandbox;
    private List<String> arguments;
    private List<InputFile> inputFiles;
    private List<OutputFile> outputFiles;
    private List<RuntimeData> runtimeData;
    private List<Link> links;

    public Task() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("runtime_data")
    public List<RuntimeData> getRuntimeData() {
        return runtimeData;
    }

    @JsonProperty("runtime_data")
    public void setRuntimeData(List<RuntimeData> runtimeData) {
        this.runtimeData = runtimeData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("last_change")
    public String getLastChange() {
        return lastChange;
    }

    @JsonProperty("last_change")
    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
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

    @JsonProperty("input_files")
    public List<InputFile> getInputFiles() {
        return inputFiles;
    }

    @JsonProperty("input_files")
    public void setInputFiles(List<InputFile> inputFiles) {
        this.inputFiles = inputFiles;
    }

    @JsonProperty("output_files")
    public List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    @JsonProperty("output_files")
    public void setOutputFiles(List<OutputFile> outputFiles) {
        this.outputFiles = outputFiles;
    }

    @JsonProperty("_links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getUploadURLAsString() {
        if (links != null) {
            for (Link l : links) {
                if (l.getRel().equals("input")) {
                    return l.getHref();
                }
            }
        }
        return null;
    }

}
