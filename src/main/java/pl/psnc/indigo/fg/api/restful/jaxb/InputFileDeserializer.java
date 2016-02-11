/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import java.io.IOException;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("unchecked")
class InputFileDeserializer extends JsonDeserializer<InputFile> {

    @Override
    public InputFile deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();
//        Map<String, Object> map = jp.readValueAs(Map.class);
//
        InputFile inputFile = new InputFile();
        if(node.isObject()) {
            // TODO: check what will happen here if we get file name and status of the file
            // we have to wait for the final version of server side results
            // at the moment we return null values
        } else {
            inputFile.setName(node.getTextValue());
        }
        
        return inputFile;
    }

}
