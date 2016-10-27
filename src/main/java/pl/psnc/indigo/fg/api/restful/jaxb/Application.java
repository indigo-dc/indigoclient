package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

import java.io.Serializable;
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
public class Application implements Serializable {
    private static final long serialVersionUID = -3560288874659978838L;

    private String id;
    private String description;
    private String name;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    private List<Infrastructure> infrastructures;
    private Outcome outcome;
    private boolean enabled;
    private List<Parameter> parameters;

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
                                  .append(id, other.id)
                                  .append(description, other.description)
                                  .append(name, other.name)
                                  .append(date, other.date)
                                  .append(infrastructures,
                                          other.infrastructures)
                                  .append(outcome, other.outcome)
                                  .append(parameters, other.parameters)
                                  .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(id).append(description).append(name)
                                    .append(date).append(infrastructures)
                                    .append(outcome).append(enabled)
                                    .append(parameters).toHashCode();
    }
}
