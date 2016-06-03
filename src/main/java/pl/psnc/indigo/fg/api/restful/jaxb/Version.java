package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author michalo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {
    private String status;
    private String updated;
    private String build;
    private MediaType mediaTypes;
    private List<Link> links;
    private String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @JsonProperty("build:")
    public String getBuild() {
        return build;
    }

    @JsonProperty("build:")
    public void setBuild(String build) {
        this.build = build;
    }

    @JsonProperty("media-types")
    public MediaType getMediaTypes() {
        return mediaTypes;
    }

    @JsonProperty("media-types")
    public void setMediaTypes(MediaType mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    @JsonProperty("_links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Version{" +
                "status='" + status + '\'' +
                ", updated='" + updated + '\'' +
                ", build='" + build + '\'' +
                ", mediaTypes=" + mediaTypes +
                ", links=" + links +
                ", id='" + id + '\'' +
                '}';
    }
}
