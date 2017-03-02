package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A bean storing information about links to resources.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {
    private String rel = "";
    private String href = "";

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Link other = (Link) o;
        return new EqualsBuilder().append(rel, other.rel)
                                  .append(href, other.href).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(rel).append(href).toHashCode();
    }
}
