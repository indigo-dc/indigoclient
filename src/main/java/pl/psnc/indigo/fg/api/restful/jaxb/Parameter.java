package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * A bean containing name, value and description of a parameter.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter implements Serializable {
    private static final long serialVersionUID = -5116819489641098653L;

    private String name;
    private String value;
    private String description;

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Parameter other = (Parameter) o;
        return new EqualsBuilder().append(name, other.name)
                                  .append(value, other.value)
                                  .append(description, other.description)
                                  .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(name).append(value)
                                    .append(description).toHashCode();
    }
}
