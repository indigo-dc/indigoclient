package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.psnc.indigo.fg.api.restful.jaxb.serialization.OutputFileDeserializer;

import java.io.Serializable;

/**
 * @author michalo
 *         <p>
 *         POJO class for storing OutputFile description
 */
@JsonDeserialize(using = OutputFileDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFile implements Serializable {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "OutputFile{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
