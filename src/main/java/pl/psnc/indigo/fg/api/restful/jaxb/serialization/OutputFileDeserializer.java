package pl.psnc.indigo.fg.api.restful.jaxb.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;


/**
 * Deserializes information about output files from JSON objects.
 */
public final class OutputFileDeserializer extends JsonDeserializer<OutputFile> {
    @Override
    public OutputFile deserialize(final JsonParser jsonParser,
                                  final DeserializationContext
                                          deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        OutputFile outputFile = new OutputFile();

        if (node.isObject()) {
            String name = node.get("name").asText();
            String url = node.get("url").asText();
            URI uri = UriBuilder.fromUri(url).build();
            outputFile.setName(name);
            outputFile.setUrl(uri);
        } else {
            String name = node.asText();
            outputFile.setName(name);
        }

        return outputFile;
    }

}
