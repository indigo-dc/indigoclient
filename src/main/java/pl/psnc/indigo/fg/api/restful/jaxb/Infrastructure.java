package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Infrastructure implements Serializable {
    private String id;
    private String name;
    private String description;
    private Date date;
    private boolean enabled;
    private boolean virtual;
    private List<Parameter> parameters;

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final Date getDate() {
        return date;
    }

    public final void setDate(final Date date) {
        this.date = date;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public final boolean isVirtual() {
        return virtual;
    }

    public final void setVirtual(final boolean virtual) {
        this.virtual = virtual;
    }

    public final List<Parameter> getParameters() {
        return parameters;
    }

    public final void setParameters(final List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public final String toString() {
        return "Infrastructure{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", date=" + date
                + ", enabled=" + enabled
                + ", virtual=" + virtual
                + ", parameters=" + parameters
                + '}';
    }
}
