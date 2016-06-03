package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author michalo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {
    private List<Link> links;
    private List<Version> versions;

    @JsonProperty("_links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "Root{" +
                "links=" + links +
                ", versions=" + versions +
                '}';
    }
}
