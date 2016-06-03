package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.InputFileDeserializer;

import java.io.Serializable;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing InputFile description
 */
@JsonDeserialize(using = InputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputFile implements Serializable {
    private String name;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InputFile{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
