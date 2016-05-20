package pl.psnc.indigo.fg.api.restful.jaxb;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tzok on 20.05.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application implements Serializable {
    public enum Outcome {
        JOB, RESOURCE
    }

    private String id;
    private String description;
    private String name;
    private Date date;
    private List<String> infrastructures;
    private Outcome outcome;
    private boolean enabled;
    private List<Parameter> parameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getInfrastructures() {
        return infrastructures;
    }

    public void setInfrastructures(List<String> infrastructures) {
        this.infrastructures = infrastructures;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", infrastructures=" + infrastructures +
                ", outcome=" + outcome +
                ", enabled=" + enabled +
                ", parameters=" + parameters +
                '}';
    }
}
