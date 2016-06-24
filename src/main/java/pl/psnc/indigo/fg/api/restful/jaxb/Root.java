package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
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
        return Collections.unmodifiableList(links);
    }

    @JsonProperty("_links")
    public final void setLinks(final List<Link> links) {
        this.links = new ArrayList<>(links);
    }

    public final List<Version> getVersions() {
        return Collections.unmodifiableList(versions);
    }

    public final void setVersions(final List<Version> versions) {
        this.versions = new ArrayList<>(versions);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Root root = (Root) o;

        return new EqualsBuilder().append(links, root.links)
                                  .append(versions, root.versions).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(links).append(versions)
                                    .toHashCode();
    }

    @Override
    public final String toString() {
        return "Root{" + "links=" + links + ", versions=" + versions + '}';
    }
}
