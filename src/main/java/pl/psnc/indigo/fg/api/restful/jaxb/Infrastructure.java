package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A bean containing description of infrastructure as configured in Future
 * Gateway.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Infrastructure implements Serializable {
    private static final long serialVersionUID = 653000622811995083L;
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
        return (Date) date.clone();
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
        return Collections.unmodifiableList(parameters);
    }

    public final void setParameters(final List<Parameter> parameters) {
        this.parameters = new ArrayList<>(parameters);
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name)
                                        .append("description", description)
                                        .append("date", date)
                                        .append("enabled", enabled)
                                        .append("virtual", virtual)
                                        .append("parameters", parameters)
                                        .toString();
    }
}
