package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing RuntimeData description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeData {
    private String name;
    private String value;
    private String description;
    private String creation;
    private String lastChange;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    @JsonProperty("last_change")
    public String getLastChange() {
        return lastChange;
    }

    @JsonProperty("last_change")
    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "RuntimeData{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", creation='" + creation + '\'' +
                ", lastChange='" + lastChange + '\'' +
                '}';
    }
}
