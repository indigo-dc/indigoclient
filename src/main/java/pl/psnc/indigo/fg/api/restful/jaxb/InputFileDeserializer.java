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
class InputFileDeserializer extends JsonDeserializer<InputFile> {
    @Override
    public InputFile deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();
        InputFile inputFile = new InputFile();
        if (node.isObject()) {
            // TODO: check what will happen here if we get file name and status of the file
            // we have to wait for the final version of server side results
            // at the moment we return null values
        } else {
            inputFile.setName(node.asText());
        }

        return inputFile;
    }

}
