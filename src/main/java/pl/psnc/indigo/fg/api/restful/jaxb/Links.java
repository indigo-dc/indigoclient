package pl.psnc.indigo.fg.api.restful.jaxb;

import java.beans.*;
import java.io.Serializable;

public class Links implements Serializable {
    
    private String rel;
    private String href;
    
    public Links() {
        
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
