package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization
        .LocalDateTimeDeserializer;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A bean class to store information about application configured on Future
 * Gateway.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    private String id;
    private String name;
    private String description;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creation;
    private List<Parameter> parameters;
    @JsonProperty("input_files")
    private List<InputFile> inputFiles;
    private List<Infrastructure> infrastructures;
    private Outcome outcome;
    private boolean enabled;
    @JsonProperty("_links")
    private List<Link> links;

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
}
