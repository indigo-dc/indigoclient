package pl.psnc.indigo.fg.api.restful.jaxb;

import java.beans.*;
import java.io.Serializable;

/**
 * 
 * @author michalo
 * 
 * POJO class for storing Links description
 * 
 */
public class Link implements Serializable {
    
    private String rel;
    private String href;
    
    public Link() {
        
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
    
    
    
}
