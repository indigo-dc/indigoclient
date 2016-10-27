package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A bean representing status of file upload.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Upload implements Serializable {
    private static final long serialVersionUID = 7633975185368095752L;

    private List<InputFile> files = Collections.emptyList();
    private String message;
    private String task;
    private String status;

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
}
