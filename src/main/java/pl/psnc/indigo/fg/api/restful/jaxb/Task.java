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
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task implements Serializable {
    public enum Status {
        SUBMIT, SUBMITTED, WAITING, READY, SCHEDULED, RUNNING, DONE, ABORTED,
        CANCELLED
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

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    @JsonProperty("runtime_data")
    public final List<RuntimeData> getRuntimeData() {
        return runtimeData;
    }

    @JsonProperty("runtime_data")
    public final void setRuntimeData(final List<RuntimeData> runtimeData) {
        this.runtimeData = runtimeData;
    }

    public final Date getDate() {
        return date;
    }

    public final void setDate(final Date date) {
        this.date = date;
    }

    @JsonProperty("last_change")
    public final Date getLastChange() {
        return lastChange;
    }

    @JsonProperty("last_change")
    public final void setLastChange(final Date lastChange) {
        this.lastChange = lastChange;
    }

    public final String getApplication() {
        return application;
    }

    public final void setApplication(final String application) {
        this.application = application;
    }

    public final String getInfrastructureTask() {
        return infrastructureTask;
    }

    public final void setInfrastructureTask(final String infrastructureTask) {
        this.infrastructureTask = infrastructureTask;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final Status getStatus() {
        return status;
    }

    public final void setStatus(final Status status) {
        this.status = status;
    }

    public final String getUser() {
        return user;
    }

    public final void setUser(final String user) {
        this.user = user;
    }

    public final String getCreation() {
        return creation;
    }

    public final void setCreation(final String creation) {
        this.creation = creation;
    }

    public final String getIosandbox() {
        return iosandbox;
    }

    public final void setIosandbox(final String iosandbox) {
        this.iosandbox = iosandbox;
    }

    public final List<String> getArguments() {
        return arguments;
    }

    public final void setArguments(final List<String> arguments) {
        this.arguments = arguments;
    }

    @JsonProperty("input_files")
    public final List<InputFile> getInputFiles() {
        return inputFiles;
    }

    @JsonProperty("input_files")
    public final void setInputFiles(final List<InputFile> inputFiles) {
        this.inputFiles = inputFiles;
    }

    @JsonProperty("output_files")
    public final List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    @JsonProperty("output_files")
    public final void setOutputFiles(final List<OutputFile> outputFiles) {
        this.outputFiles = outputFiles;
    }

    @JsonProperty("_links")
    public final List<Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public final void setLinks(final List<Link> links) {
        this.links = links;
    }

    @Override
    public final String toString() {
        return "Task{"
                + "id='" + id + '\''
                + ", date=" + date
                + ", lastChange=" + lastChange
                + ", application='" + application + '\''
                + ", infrastructureTask='" + infrastructureTask + '\''
                + ", description='" + description + '\''
                + ", status=" + status
                + ", user='" + user + '\''
                + ", arguments=" + arguments
                + ", inputFiles=" + inputFiles
                + ", outputFiles=" + outputFiles
                + ", runtimeData=" + runtimeData
                + ", creation='" + creation + '\''
                + ", iosandbox='" + iosandbox + '\''
                + ", links=" + links
                + '}';
    }
}
