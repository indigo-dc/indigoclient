package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * A class containing key-value pair used in {@link PatchRuntimeData}.
 */
@Getter
@Setter
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyValue {
    @JsonProperty("data_name") private String dataName = "";
    @JsonProperty("data_value") private String dataValue = "";

    /**
     * Construct an empty instance. Used by bean instantiation.
     */
    public KeyValue() {
        super();
    }

    /**
     * Construct an instance with known values.
     *
     * @param dataName  Key name.
     * @param dataValue Value.
     */
    public KeyValue(final String dataName, final String dataValue) {
        super();
        this.dataName = dataName;
        this.dataValue = dataValue;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        KeyValue keyValue = (KeyValue) o;
        return Objects.equals(dataName, keyValue.dataName) &&
               Objects.equals(dataValue, keyValue.dataValue);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(dataName, dataValue);
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("dataName", dataName).append("dataValue", dataValue)
                .toString();
    }
}
