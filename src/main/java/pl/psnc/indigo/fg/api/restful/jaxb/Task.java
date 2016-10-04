package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A bean containing full description of a task to be run via Future Gateway.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task implements Serializable {
    private static final long serialVersionUID = 3419851374883611721L;

    /**
     * All possible states a task can be seen by the Future Gateway.
     */
    public enum Status {
        SUBMIT, SUBMITTED, WAITING, READY, SCHEDULED, RUNNING, DONE, ABORTED,
        CANCELLED, PROCESSING
    }

    private String id;
    private Date date = new Date(0);
    private Date lastChange = new Date(0);
    private String application;
    private String infrastructureTask;
    private String description;
    private Status status;
    private String user;
    private List<String> arguments = Collections.emptyList();
    private List<InputFile> inputFiles = Collections.emptyList();
    private List<OutputFile> outputFiles = Collections.emptyList();
    private List<RuntimeData> runtimeData = Collections.emptyList();
    private String creation;
    private String iosandbox;
    private List<Link> links = Collections.emptyList();

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    @JsonProperty("runtime_data")
    public final List<RuntimeData> getRuntimeData() {
        return Collections.unmodifiableList(runtimeData);
    }

    @JsonProperty("runtime_data")
    public final void setRuntimeData(final List<RuntimeData> runtimeData) {
        this.runtimeData = new ArrayList<>(runtimeData);
    }

    public final Date getDate() {
        return (Date) date.clone();
    }

    public final void setDate(final Date date) {
        this.date = date;
    }

    @JsonProperty("last_change")
    public final Date getLastChange() {
        return (Date) lastChange.clone();
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
        return Collections.unmodifiableList(arguments);
    }

    public final void setArguments(final List<String> arguments) {
        this.arguments = new ArrayList<>(arguments);
    }

    @JsonProperty("input_files")
    public final List<InputFile> getInputFiles() {
        return Collections.unmodifiableList(inputFiles);
    }

    @JsonProperty("input_files")
    public final void setInputFiles(final List<InputFile> inputFiles) {
        this.inputFiles = new ArrayList<>(inputFiles);
    }

    @JsonProperty("output_files")
    public final List<OutputFile> getOutputFiles() {
        return Collections.unmodifiableList(outputFiles);
    }

    @JsonProperty("output_files")
    public final void setOutputFiles(final List<OutputFile> outputFiles) {
        this.outputFiles = new ArrayList<>(outputFiles);
    }

    @JsonProperty("_links")
    public final List<Link> getLinks() {
        return Collections.unmodifiableList(links);
    }

    @JsonProperty("_links")
    public final void setLinks(final List<Link> links) {
        this.links = new ArrayList<>(links);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Task task = (Task) o;

        return new EqualsBuilder().append(id, task.id).append(date, task.date)
                                  .append(lastChange, task.lastChange)
                                  .append(application, task.application)
                                  .append(infrastructureTask,
                                          task.infrastructureTask)
                                  .append(description, task.description)
                                  .append(status, task.status)
                                  .append(user, task.user)
                                  .append(arguments, task.arguments)
                                  .append(inputFiles, task.inputFiles)
                                  .append(outputFiles, task.outputFiles)
                                  .append(runtimeData, task.runtimeData)
                                  .append(creation, task.creation)
                                  .append(iosandbox, task.iosandbox)
                                  .append(links, task.links).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(id).append(date).append(lastChange)
                                    .append(application)
                                    .append(infrastructureTask)
                                    .append(description).append(status)
                                    .append(user).append(arguments)
                                    .append(inputFiles).append(outputFiles)
                                    .append(runtimeData).append(creation)
                                    .append(iosandbox).append(links)
                                    .toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id).append("date", date)
                .append("lastChange", lastChange)
                .append("application", application)
                .append("infrastructureTask", infrastructureTask)
                .append("description", description).append("status", status)
                .append("user", user).append("arguments", arguments)
                .append("inputFiles", inputFiles)
                .append("outputFiles", outputFiles)
                .append("runtimeData", runtimeData).append("creation", creation)
                .append("iosandbox", iosandbox).append("links", links)
                .toString();
    }
}
