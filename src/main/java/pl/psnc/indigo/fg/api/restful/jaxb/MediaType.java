/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author michalo
 */
@FutureGatewayBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaType {
    private String type;

    public final String getType() {
        return type;
    }

    public final void setType(final String type) {
        this.type = type;
    }

    @Override
    public final String toString() {
        return "MediaType{type='" + type + "'}";
    }
}
