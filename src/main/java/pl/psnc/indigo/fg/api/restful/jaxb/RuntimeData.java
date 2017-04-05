package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

/**
 * A bean containing general purpose runtime data.
 */
@Getter
@Setter
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeData {
    private String name = "";
    private String value = "";
    private String description = "";
    private String creation = "";
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastChange = LocalDateTime.now();

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
        return new HashCodeBuilder().append(name).append(value)
                                    .append(description).append(creation)
                                    .append(lastChange).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name).append("value", value)
                .append("description", description).append("creation", creation)
                .append("lastChange", lastChange).toString();
    }
}
