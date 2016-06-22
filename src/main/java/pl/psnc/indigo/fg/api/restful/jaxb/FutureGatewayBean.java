package pl.psnc.indigo.fg.api.restful.jaxb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used later in a class which translates between beans and
 * Kepler's RecordToken.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FutureGatewayBean {
}
