package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collections;
import java.util.List;

/**
 * A bean representing status of file upload.
 */
@Getter
@Setter
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Upload {
    private List<InputFile> files = Collections.emptyList();
    private String message = "";
    private String task = "";
    @JsonProperty("gestatus") private String status = "";

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Upload other = (Upload) o;
        return new EqualsBuilder().append(files, other.files)
                                  .append(message, other.message)
                                  .append(task, other.task)
                                  .append(status, other.status).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(files).append(message).append(task)
                                    .append(status).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("files", files).append("message", message)
                .append("task", task).append("status", status).toString();
    }
}
