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

/**
 * A bean containing general purpose runtime data.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeData implements Serializable {
    private static final long serialVersionUID = 2473352732801435794L;

    private String name;
    private String value;
    private String description;
    private String creation;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastChange;

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        RuntimeData other = (RuntimeData) o;
        return new EqualsBuilder().append(name, other.name)
                                  .append(value, other.value)
                                  .append(description, other.description)
                                  .append(creation, other.creation)
                                  .append(lastChange, other.lastChange)
                                  .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(value)
                                          .append(description).append(creation)
                                          .append(lastChange).toHashCode();
    }
}
