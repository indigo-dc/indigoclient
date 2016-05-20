/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

@SuppressWarnings("unchecked")
class OutputFileDeserializer extends JsonDeserializer<OutputFile> {

    @Override
    public OutputFile deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();
        OutputFile outputFile = new OutputFile();
        if (node.isObject()) {
            JsonNode name = node.get("name");
            JsonNode url = node.get("url");

            outputFile.setName(name.getTextValue());
            outputFile.setUrl(url.getTextValue());
        } else {
            outputFile.setName(node.getTextValue());
        }

        return outputFile;
    }

}
