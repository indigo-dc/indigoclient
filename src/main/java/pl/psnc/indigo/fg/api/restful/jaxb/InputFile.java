package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.InputFileDeserializer;

import java.io.Serializable;

/**
 * A bean storing information about input files used in job submission and
 * status checking.
 */
@FutureGatewayBean
@JsonDeserialize(using = InputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputFile implements Serializable {
    private static final long serialVersionUID = -8629464708321890767L;
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
        return new ToStringBuilder(this).append("name", name)
                                        .append("status", status).toString();
    }
}
