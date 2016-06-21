package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.OutputFileDeserializer;

import java.io.Serializable;
import java.net.URI;

/**
 * A bean containing information about name and URL of an output file.
 */
@FutureGatewayBean
@JsonDeserialize(using = OutputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFile implements Serializable {
    private static final long serialVersionUID = -6923395958538037455L;
    private String name;
    private URI url;

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final URI getUrl() {
        return url;
    }

    public final void setUrl(final URI url) {
        this.url = url;
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this).append("name", name).append("url", url)
                                        .toString();
    }
}
