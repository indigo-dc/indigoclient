package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InputFile inputFile = (InputFile) o;

        return new EqualsBuilder().append(name, inputFile.name)
                                  .append(status, inputFile.status).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(name).append(status).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name).append("status", status).toString();
    }
}
