package pl.psnc.indigo.fg.api.restful.jaxb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * A bean representing JSON response from Token service, part of
 * indigo-dc/LiferayPlugins.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenServiceResponse {
    private String token;
    private String error;
}
