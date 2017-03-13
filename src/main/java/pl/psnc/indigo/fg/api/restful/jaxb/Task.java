package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization
        .LocalDateTimeDeserializer;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * A bean containing full description of a task to be run via Future Gateway.
 */
@Getter
@Setter
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private String id = "";
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date = LocalDateTime.now();
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("last_change")
    private LocalDateTime lastChange = LocalDateTime.now();
    private String application = "";
    private String infrastructureTask = "";
    private String description = "";
    private TaskStatus status = TaskStatus.UNKNOWN;
    private String user = "";
    private List<String> arguments = Collections.emptyList();
    @JsonProperty("input_files")
    private List<InputFile> inputFiles = Collections.emptyList();
    @JsonProperty("output_files")
    private List<OutputFile> outputFiles = Collections.emptyList();
    @JsonProperty("runtime_data")
    private List<RuntimeData> runtimeData = Collections.emptyList();
    private String creation = "";
    private String iosandbox = "";
    @JsonProperty("_links")
    private List<Link> links = Collections.emptyList();

    public final boolean isDone() {
        return status == TaskStatus.DONE;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Task other = (Task) o;
        return new EqualsBuilder().append(id, other.id).append(date, other.date)
                                  .append(lastChange, other.lastChange)
                                  .append(application, other.application)
                                  .append(infrastructureTask,
                                          other.infrastructureTask)
                                  .append(description, other.description)
                                  .append(status, other.status)
                                  .append(user, other.user)
                                  .append(arguments, other.arguments)
                                  .append(inputFiles, other.inputFiles)
                                  .append(outputFiles, other.outputFiles)
                                  .append(runtimeData, other.runtimeData)
                                  .append(creation, other.creation)
                                  .append(iosandbox, other.iosandbox)
                                  .append(links, other.links).isEquals();
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
    public String toString() {
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
