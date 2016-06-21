/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A bean containing information about MIME media type of a resource.
 */
@SuppressWarnings("WeakerAccess")
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
        return new ToStringBuilder(this).append("type", type).toString();
    }
}
