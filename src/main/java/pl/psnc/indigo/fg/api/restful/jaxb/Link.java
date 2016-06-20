package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing Links description
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link implements Serializable {
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
        return "Link{"
                + "rel='" + rel + '\''
                + ", href='" + href + '\''
                + '}';
    }
}
