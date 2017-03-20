package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A bean containing general purpose runtime data which are used only when
 * PATCH-ing a task.
 */
@Getter
@Setter
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatchRuntimeData {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("runtimeData", runtimeData).toString();
    }
}
