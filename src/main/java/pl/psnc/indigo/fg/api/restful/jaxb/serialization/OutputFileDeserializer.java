package pl.psnc.indigo.fg.api.restful.jaxb.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class OutputFileDeserializer extends JsonDeserializer<OutputFile> {
    @Override
    public OutputFile deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        JsonNode node = jp.readValueAsTree();
        OutputFile outputFile = new OutputFile();

        if (node.isObject()) {
            outputFile.setName(node.get("name").asText());
            outputFile.setUrl(node.get("url").asText());
        } else {
            outputFile.setName(node.asText());
        }

        return outputFile;
    }

}
