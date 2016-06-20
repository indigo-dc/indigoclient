package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.OutputFileDeserializer;

import java.io.Serializable;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing OutputFile description
 */
@FutureGatewayBean
@JsonDeserialize(using = OutputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFile implements Serializable {
    private String name;
    private String url;

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getUrl() {
        return url;
    }

    public final void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public final String toString() {
        return "OutputFile{"
                + "name='" + name + '\''
                + ", url='" + url + '\''
                + '}';
    }
}
