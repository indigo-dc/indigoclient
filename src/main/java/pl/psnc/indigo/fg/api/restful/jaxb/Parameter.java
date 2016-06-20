package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter implements Serializable {
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
        return "Parameter{"
                + "name='" + name + '\''
                + ", value='" + value + '\''
                + ", description='" + description + '\''
                + '}';
    }
}
