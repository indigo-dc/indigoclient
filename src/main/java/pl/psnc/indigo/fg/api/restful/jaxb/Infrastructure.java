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
 * A bean containing description of infrastructure as configured in Future
 * Gateway.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Infrastructure implements Serializable {
    private static final long serialVersionUID = 653000622811995083L;

    private String id;
    private String name;
    private String description;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    private boolean enabled;
    private boolean virtual;
    private List<Parameter> parameters;

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Infrastructure other = (Infrastructure) o;
        return new EqualsBuilder().append(enabled, other.enabled)
                                  .append(virtual, other.virtual)
                                  .append(id, other.id).append(name, other.name)
                                  .append(description, other.description)
                                  .append(date, other.date)
                                  .append(parameters, other.parameters)
                                  .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name)
                                          .append(description).append(date)
                                          .append(enabled).append(virtual)
                                          .append(parameters).toHashCode();
    }
}
