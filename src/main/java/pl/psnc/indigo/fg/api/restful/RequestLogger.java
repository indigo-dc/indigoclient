package pl.psnc.indigo.fg.api.restful;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

/**
 * A filter for JAX-RS requests which will log both headers and body of HTTP
 * requests.
 */
public class RequestLogger implements ClientRequestFilter {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RequestLogger.class);
    private static final int ONE_KB = 1024;

    @Override
    public final void filter(final ClientRequestContext clientRequestContext) {
        StringBuilder builder = new StringBuilder(RequestLogger.ONE_KB);
        RequestLogger.describeHeaders(clientRequestContext, builder);
        builder.append(System.lineSeparator());
        RequestLogger.describeBody(clientRequestContext, builder);
        RequestLogger.LOGGER.trace(builder.toString());
    }

    /**
     * Fill information about the body of message i.e. its content of
     * filename in case of file upload.
     *
     * @param context Context of the HTTP request.
     * @param builder Where the output will be appended.
     */
    private static void describeBody(final ClientRequestContext context,
                                     final StringBuilder builder) {
        if ((context != null) && context.hasEntity()) {
            Object entity = context.getEntity();

            if (entity instanceof MultiPart) {
                for (final BodyPart part : ((MultiPart) entity)
                        .getBodyParts()) {
                    if (part instanceof FileDataBodyPart) {
                        builder.append(
                                ((FileDataBodyPart) part).getFileEntity());
                    } else {
                        builder.append(part);
                    }
                }
            } else {
                builder.append(entity);
            }
        }
    }

    /**
     * Fill information about the headers of an HTTP request.
     *
     * @param context Context of the HTTP request.
     * @param builder Where the output will be appended.
     */
    private static void describeHeaders(final ClientRequestContext context,
                                        final StringBuilder builder) {
        MultivaluedMap<String, String> headers = context.getStringHeaders();
        for (final Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            builder.append(key);
            builder.append(": ");

            for (final String value : entry.getValue()) {
                builder.append(value);
                builder.append(',');
            }

            builder.append(System.lineSeparator());
        }
    }
}
