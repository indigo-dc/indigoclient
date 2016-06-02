/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

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

            outputFile.setName(name.asText());
            outputFile.setUrl(url.asText());
        } else {
            outputFile.setName(node.asText());
        }

        return outputFile;
    }

}
