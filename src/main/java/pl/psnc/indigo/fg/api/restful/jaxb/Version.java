/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author michalo
 */
public class Version {
   String status;
   String updated;
   MediaType mediaTypes;
   List<Link> links;
   String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @JsonProperty("media-types")
    public MediaType getMediaTypes() {
        return mediaTypes;
    }

    @JsonProperty("media-types")
    public void setMediaTypes(MediaType mediaTypes) {
        this.mediaTypes = mediaTypes;
    }
    
    @JsonProperty("_links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   
   
   
}
