package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A bean containing general purpose runtime data which are used only when
 * PATCH-ing a task.
 */
@Getter
@Setter
@ToString
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatchRuntimeData {
    public static class KeyValue {
        @JsonProperty("data_name")
        private String dataName = "";
        @JsonProperty("data_value")
        private String dataValue = "";

        @Override
        public final boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            KeyValue keyValue = (KeyValue) o;
            return Objects.equals(dataName, keyValue.dataName) && Objects
                    .equals(dataValue, keyValue.dataValue);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(dataName, dataValue);
        }
    }

    @JsonProperty("runtime_data")
    private List<KeyValue> runtimeData = Collections.emptyList();

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        PatchRuntimeData other = (PatchRuntimeData) o;
        return Objects.equals(runtimeData, other.runtimeData);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(runtimeData);
    }
}
