package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * A bean containing general purpose runtime data.
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
class RuntimeData implements Serializable {
    private static final long serialVersionUID = 2473352732801435794L;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RuntimeData that = (RuntimeData) o;

        return new EqualsBuilder().append(name, that.name)
                                  .append(value, that.value)
                                  .append(description, that.description)
                                  .append(creation, that.creation)
                                  .append(lastChange, that.lastChange)
                                  .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(value)
                                          .append(description).append(creation)
                                          .append(lastChange).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this).append("name", name)
                                        .append("value", value)
                                        .append("description", description)
                                        .append("creation", creation)
                                        .append("lastChange", lastChange)
                                        .toString();
    }
}
