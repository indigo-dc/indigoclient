/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author michalo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
