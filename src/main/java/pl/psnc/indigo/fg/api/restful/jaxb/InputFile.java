package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.InputFileDeserializer;

import java.io.Serializable;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing InputFile description
 */
@FutureGatewayBean
@JsonDeserialize(using = InputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputFile implements Serializable {
    private String name;
    private String status;

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getStatus() {
        return status;
    }

    public final void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public final String toString() {
        return "InputFile{"
                + "name='" + name + '\''
                + ", status='" + status + '\''
                + '}';
    }
}
