package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author michalo
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {
    private List<Link> links;
    private List<Version> versions;

    @JsonProperty("_links")
    public final List<Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public final void setLinks(final List<Link> links) {
        this.links = links;
    }

    public final List<Version> getVersions() {
        return versions;
    }

    public final void setVersions(final List<Version> versions) {
        this.versions = versions;
    }

    @Override
    public final String toString() {
        return "Root{"
                + "links=" + links
                + ", versions=" + versions
                + '}';
    }
}
