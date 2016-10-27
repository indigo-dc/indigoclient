package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.MediaTypeDeserializer;

import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;

/**
 * A bean containing information about version of the Future Gateway.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version implements Serializable {
    private static final long serialVersionUID = -3831342777464028231L;

    private String status;
    private String updated;
    @JsonProperty("build:")
    private String build;
    @JsonDeserialize(using = MediaTypeDeserializer.class)
    @JsonProperty("media-types")
    private MediaType mediaType;
    @JsonProperty("_links")
    private List<Link> links;
    private String id;

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Version other = (Version) o;
        return new EqualsBuilder().append(status, other.status)
                                  .append(updated, other.updated)
                                  .append(build, other.build)
                                  .append(mediaType, other.mediaType)
                                  .append(links, other.links)
                                  .append(id, other.id).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(status).append(updated)
                                    .append(build).append(mediaType)
                                    .append(links).append(id).toHashCode();
    }
}
