package pl.psnc.indigo.fg.api.restful.jaxb.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;

import java.io.IOException;
import java.net.URI;


/**
 * Deserializes information about output files from JSON objects.
 */
public final class OutputFileDeserializer extends JsonDeserializer<OutputFile> {
    @Override
    public OutputFile deserialize(
            final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        JsonNode nameNode = node.get("name");
        String name = nameNode.asText();
        JsonNode urlNode = node.get("url");
        String url = urlNode.asText();

        OutputFile outputFile = new OutputFile();
        outputFile.setName(name);
        outputFile.setUrl(URI.create(url));
        return outputFile;
    }
}
