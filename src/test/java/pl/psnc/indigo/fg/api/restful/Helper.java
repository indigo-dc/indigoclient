package pl.psnc.indigo.fg.api.restful;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Helper {
    public static String readResource(final String resource)
            throws IOException {
        ClassLoader classLoader = Helper.class.getClassLoader();
        try (InputStream stream = classLoader.getResourceAsStream(resource)) {
            return IOUtils.toString(stream, Charset.defaultCharset());
        }
    }

    private Helper() {
        super();
    }
}
