package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author michalo
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {
    private String status;
    private String updated;
    private String build;
    private MediaType mediaTypes;
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
    public final MediaType getMediaTypes() {
        return mediaTypes;
    }

    @JsonProperty("media-types")
    public final void setMediaTypes(final MediaType mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    @JsonProperty("_links")
    public final List<Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public final void setLinks(final List<Link> links) {
        this.links = links;
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    @Override
    public final String toString() {
        return "Version{"
                + "status='" + status + '\''
                + ", updated='" + updated + '\''
                + ", build='" + build + '\''
                + ", mediaTypes=" + mediaTypes
                + ", links=" + links
                + ", id='" + id + '\''
                + '}';
    }
}
