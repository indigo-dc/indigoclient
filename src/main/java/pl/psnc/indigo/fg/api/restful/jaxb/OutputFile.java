package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.OutputFileDeserializer;

import java.net.URI;

/**
 * A bean containing information about name and URL of an output file.
 */
@Getter
@Setter
@FutureGatewayBean
@JsonDeserialize(using = OutputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFile {
    private String name = "";
    private URI url = URI.create("");

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        OutputFile other = (OutputFile) o;
        return new EqualsBuilder().append(name, other.name)
                                  .append(url, other.url).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(name).append(url).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name).append("url", url).toString();
    }
}
