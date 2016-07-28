package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A bean class to store information about application configured on Future
 * Gateway.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application implements Serializable {
    private static final long serialVersionUID = -3560288874659978838L;

    /**
     * Types of outcome an application can return.
     */
    @SuppressWarnings("WeakerAccess")
    public enum Outcome {
        JOB, RESOURCE
    }

    private String id;
    private String description;
    private String name;
    private Date date;
    private List<Infrastructure> infrastructures;
    private Outcome outcome;
    private boolean enabled;
    private List<Parameter> parameters;

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final Date getDate() {
        return (Date) date.clone();
    }

    public final void setDate(final Date date) {
        this.date = date;
    }

    public final List<Infrastructure> getInfrastructures() {
        return Collections.unmodifiableList(infrastructures);
    }

    public final void setInfrastructures(
            final List<Infrastructure> infrastructures) {
        this.infrastructures = new ArrayList<>(infrastructures);
    }

    public final Outcome getOutcome() {
        return outcome;
    }

    public final void setOutcome(final Outcome outcome) {
        this.outcome = outcome;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public final List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public final void setParameters(final List<Parameter> parameters) {
        this.parameters = new ArrayList<>(parameters);
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id).append("description", description)
                .append("name", name).append("date", date.toInstant())
                .append("infrastructures", infrastructures)
                .append("outcome", outcome).append("enabled", enabled)
                .append("parameters", parameters).toString();
    }
}
