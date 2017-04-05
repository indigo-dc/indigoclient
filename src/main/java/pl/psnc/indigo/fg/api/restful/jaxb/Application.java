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
 * A bean class to store information about application configured on Future
 * Gateway.
 */
@Getter
@Setter
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    private String id = "";
    private String name = "";
    private String description = "";
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creation = LocalDateTime.now();
    private List<Parameter> parameters = Collections.emptyList();
    @JsonProperty("input_files")
    private List<InputFile> inputFiles = Collections.emptyList();
    private List<Infrastructure> infrastructures = Collections.emptyList();
    private Outcome outcome = Outcome.JOB;
    private boolean enabled;
    @JsonProperty("_links")
    private List<Link> links = Collections.emptyList();

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Application other = (Application) o;
        return new EqualsBuilder().append(enabled, other.enabled)
                                  .append(id, other.id).append(name, other.name)
                                  .append(description, other.description)
                                  .append(creation, other.creation)
                                  .append(parameters, other.parameters)
                                  .append(inputFiles, other.inputFiles)
                                  .append(infrastructures,
                                          other.infrastructures)
                                  .append(outcome, other.outcome)
                                  .append(links, other.links).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(description)
                                    .append(creation).append(parameters)
                                    .append(inputFiles).append(infrastructures)
                                    .append(outcome).append(enabled)
                                    .append(links).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id).append("name", name)
                .append("description", description).append("creation", creation)
                .append("parameters", parameters)
                .append("inputFiles", inputFiles)
                .append("infrastructures", infrastructures)
                .append("outcome", outcome).append("enabled", enabled)
                .append("links", links).toString();
    }
}
