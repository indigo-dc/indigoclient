package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing RuntimeData description
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeData {
    private String name;
    private String value;
    private String description;
    private String creation;
    private String lastChange;

    public final String getValue() {
        return value;
    }

    public final void setValue(final String value) {
        this.value = value;
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

    public final String getCreation() {
        return creation;
    }

    public final void setCreation(final String creation) {
        this.creation = creation;
    }

    @JsonProperty("last_change")
    public final String getLastChange() {
        return lastChange;
    }

    @JsonProperty("last_change")
    public final void setLastChange(final String lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public final String toString() {
        return "RuntimeData{"
                + "name='" + name + '\''
                + ", value='" + value + '\''
                + ", description='" + description + '\''
                + ", creation='" + creation + '\''
                + ", lastChange='" + lastChange + '\''
                + '}';
    }
}
