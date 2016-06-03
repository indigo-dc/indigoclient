package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing Task description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task implements Serializable {
    public enum Status {
        SUBMIT, SUBMITTED, WAITING, READY, SCHEDULED, RUNNING, DONE, ABORTED, CANCELLED
    }

    private String id;
    private Date date;
    private Date lastChange;
    private String application;
    private String infrastructureTask;
    private String description;
    private Status status;
    private String user;
    private List<String> arguments;
    private List<InputFile> inputFiles;
    private List<OutputFile> outputFiles;
    private List<RuntimeData> runtimeData;
    private String creation;
    private String iosandbox;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("last_change")
    public Date getLastChange() {
        return lastChange;
    }

    @JsonProperty("last_change")
    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getInfrastructureTask() {
        return infrastructureTask;
    }

    public void setInfrastructureTask(String infrastructureTask) {
        this.infrastructureTask = infrastructureTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
                if ("input".equals(l.getRel())) {
                    return l.getHref();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", lastChange=" + lastChange +
                ", application='" + application + '\'' +
                ", infrastructureTask='" + infrastructureTask + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", user='" + user + '\'' +
                ", arguments=" + arguments +
                ", inputFiles=" + inputFiles +
                ", outputFiles=" + outputFiles +
                ", runtimeData=" + runtimeData +
                ", creation='" + creation + '\'' +
                ", iosandbox='" + iosandbox + '\'' +
                ", links=" + links +
                '}';
    }
}
