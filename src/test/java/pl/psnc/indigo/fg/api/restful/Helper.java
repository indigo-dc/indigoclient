package pl.psnc.indigo.fg.api.restful;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Helper {
    private static final ClassLoader CLASS_LOADER =
            Helper.class.getClassLoader();

    public static String readResource(final String resource)
            throws IOException {
        try (InputStream stream = Helper.CLASS_LOADER
                .getResourceAsStream(resource)) {
            return IOUtils.toString(stream, Charset.defaultCharset());
        }
    }

    public static File getResourceFile(final String resource) {
        return new File(Helper.CLASS_LOADER.getResource(resource).getFile());
    }

    private Helper() {
        super();
    }
}
