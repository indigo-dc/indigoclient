package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.List;

/**
 * @author michalo
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {
    @JsonProperty("_links")
    private List<Link> links = Collections.emptyList();
    private List<Version> versions = Collections.emptyList();

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Root other = (Root) o;
        return new EqualsBuilder().append(links, other.links)
                                  .append(versions, other.versions).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(links).append(versions)
                                    .toHashCode();
    }
}
