package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.MediaTypeDeserializer;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A bean containing information about version of the Future Gateway.
 */
@FutureGatewayBean

@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {
    private String status;
    private String updated;
    private String build;
    @JsonDeserialize(using = MediaTypeDeserializer.class)
    private MediaType mediaType;
    private List<Link> links;
    private String id;

    public final String getStatus() {
        return status;
    }

    public final void setStatus(final String status) {
        this.status = status;
    }

    public final String getUpdated() {
        return updated;
    }

    public final void setUpdated(final String updated) {
        this.updated = updated;
    }

    @JsonProperty("build:")
    public final String getBuild() {
        return build;
    }

    @JsonProperty("build:")
    public final void setBuild(final String build) {
        this.build = build;
    }

    @JsonProperty("media-types")
    public final MediaType getMediaType() {
        return mediaType;
    }

    @JsonProperty("media-types")
    public final void setMediaType(final MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @JsonProperty("_links")
    public final List<Link> getLinks() {
        return Collections.unmodifiableList(links);
    }

    @JsonProperty("_links")
    public final void setLinks(final List<Link> links) {
        this.links = new ArrayList<>(links);
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Version version = (Version) o;

        return new EqualsBuilder().append(status, version.status)
                                  .append(updated, version.updated)
                                  .append(build, version.build)
                                  .append(mediaType, version.mediaType)
                                  .append(links, version.links)
                                  .append(id, version.id).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(status).append(updated)
                                    .append(build).append(mediaType)
                                    .append(links).append(id).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("status", status).append("updated", updated)
                .append("build", build).append("mediaType", mediaType)
                .append("links", links).append("id", id).toString();
    }
}
