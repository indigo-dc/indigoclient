package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * A bean storing information about links to resources.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link implements Serializable {
    private static final long serialVersionUID = 1841770335729950116L;
    private String rel;
    private String href;

    public final String getRel() {
        return rel;
    }

    public final void setRel(final String rel) {
        this.rel = rel;
    }

    public final String getHref() {
        return href;
    }

    public final void setHref(final String href) {
        this.href = href;
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("rel", rel).append("href", href).toString();
    }
}
