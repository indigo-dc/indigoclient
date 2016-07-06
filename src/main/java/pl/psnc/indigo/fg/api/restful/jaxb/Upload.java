package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A bean representing status of file upload.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Upload implements Serializable {
    private static final long serialVersionUID = 7633975185368095752L;
    private List<InputFile> files = Collections.emptyList();
    private String message;
    private String task;
    private String status;

    public final List<InputFile> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public final void setFiles(final List<InputFile> files) {
        this.files = new ArrayList<>(files);
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

    public final String getTask() {
        return task;
    }

    public final void setTask(final String task) {
        this.task = task;
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

        Upload upload = (Upload) o;

        return new EqualsBuilder().append(files, upload.files)
                                  .append(message, upload.message)
                                  .append(task, upload.task)
                                  .append(status, upload.status).isEquals();
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
