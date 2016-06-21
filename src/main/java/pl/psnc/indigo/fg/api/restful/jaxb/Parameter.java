package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * A bean containing name, value and description of a parameter.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter implements Serializable {
    private static final long serialVersionUID = -5116819489641098653L;
    private String name;
    private String value;
    private String description;

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getValue() {
        return value;
    }

    public final void setValue(final String value) {
        this.value = value;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this).append("name", name)
                                        .append("value", value)
                                        .append("description", description)
                                        .toString();
    }
}
