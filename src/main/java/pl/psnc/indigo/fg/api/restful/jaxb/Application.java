package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application implements Serializable {
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
        return date;
    }

    public final void setDate(final Date date) {
        this.date = date;
    }

    public final List<Infrastructure> getInfrastructures() {
        return infrastructures;
    }

    public final void setInfrastructures(final List<Infrastructure>
                                                 infrastructures) {
        this.infrastructures = infrastructures;
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
        return parameters;
    }

    public final void setParameters(final List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public final String toString() {
        return "Application{"
                + "id='" + id + '\''
                + ", description='" + description + '\''
                + ", name='" + name + '\''
                + ", date=" + date
                + ", infrastructures=" + infrastructures
                + ", outcome=" + outcome
                + ", enabled=" + enabled
                + ", parameters=" + parameters
                + '}';
    }
}
