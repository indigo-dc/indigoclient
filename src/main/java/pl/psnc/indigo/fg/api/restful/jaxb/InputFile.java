package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.InputFileDeserializer;

/**
 * A bean storing information about input files used in job submission and
 * status checking.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonDeserialize(using = InputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputFile {
    private String name = "";
    private String status = "";

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        InputFile other = (InputFile) o;
        return new EqualsBuilder().append(name, other.name)
                                  .append(status, other.status).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(name).append(status).toHashCode();
    }
}
