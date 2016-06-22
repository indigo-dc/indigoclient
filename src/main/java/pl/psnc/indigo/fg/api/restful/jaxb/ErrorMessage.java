package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A bean containing error message.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
class ErrorMessage {
    private String message;

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this).append("message", message).toString();
    }
}
