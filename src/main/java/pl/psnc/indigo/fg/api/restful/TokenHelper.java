package pl.psnc.indigo.fg.api.restful;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.TokenServiceResponse;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * A utility class which allows to get valid IAM token by using the special
 * Token service, part of indigo-dc/LiferayPlugins.
 */
public final class TokenHelper {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TokenHelper.class);
    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle("messages"); //NON-NLS
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Empty constructor, because this is a utility class (use only public
     * static methods).
     */
    private TokenHelper() {
        super();
    }

    /**
     * Contact Token service, authenticate with username and password and
     * obtain a valid token. If the given input token is still valid, it will
     * be given as output. Otherwise a new one will be returned.
     *
     * @param token                IAM token, can be already expired.
     * @param tokenServiceUri      {@link URI} of the Token service endpoint.
     * @param tokenServiceUser     Username for the Token service.
     * @param tokenServicePassword Password for the Token service.
     * @return An IAM token which is ensured to be valid.
     * @throws FutureGatewayException If anything goes wrong (e.g. invalid
     *                                credentials)
     */
    public static String getToken(final String token, final URI tokenServiceUri,
                                  final String tokenServiceUser,
                                  final String tokenServicePassword)
            throws FutureGatewayException {
        final String subject = TokenHelper.decodeToken(token).getSubject();
        final String format = TokenHelper.RESOURCE_BUNDLE.getString(
                "failed.to.get.token.for.user.with.subject.0.reason.1");

        try {
            TokenHelper.LOGGER.debug("POST {}", tokenServiceUri); //NON-NLS

            // header
            final Header authorizationHeader = TokenHelper
                    .getAuthorizationHeader(tokenServiceUser,
                                            tokenServicePassword);
            // body
            final BasicNameValuePair valuePair =
                    new BasicNameValuePair("subject", subject); //NON-NLS
            // request
            final HttpResponse response =
                    Request.Post(tokenServiceUri).setHeader(authorizationHeader)
                           .bodyForm(valuePair).execute().returnResponse();

            final StatusLine status = response.getStatusLine();
            final int statusCode = status.getStatusCode();
            final String reasonPhrase = status.getReasonPhrase();
            TokenHelper.LOGGER.debug(RootAPI.STATUS, statusCode, reasonPhrase);

            if (statusCode == HttpStatus.SC_OK) {
                final TokenServiceResponse tokenServiceResponse =
                        TokenHelper.readToken(response);
                final String error = tokenServiceResponse.getError();

                if (error != null) {
                    final String message =
                            MessageFormat.format(format, subject, error);
                    TokenHelper.LOGGER.error(message);
                    throw new FutureGatewayException(message);
                }

                final String result = tokenServiceResponse.getToken();
                TokenHelper.decodeToken(result);
                return result;
            } else {
                final String message =
                        MessageFormat.format(format, subject, reasonPhrase);
                TokenHelper.LOGGER.error(message);
                throw new FutureGatewayException(message);
            }
        } catch (final IOException e) {
            final String message =
                    MessageFormat.format(format, subject, e.getMessage());
            TokenHelper.LOGGER.error(message, e);
            throw new FutureGatewayException(message, e);
        }
    }

    /**
     * Create {@link Header} instance with Basic HTTP authorization scheme.
     *
     * @param user     Username.
     * @param password Password.
     * @return An instance of {@link Header} which conforms to HTTP Basic
     * auth scheme.
     */
    private static Header getAuthorizationHeader(final String user,
                                                 final String password) {
        final String encoded = Base64.encodeBase64String(
                String.format("%s:%s", user, password)
                      .getBytes(Charset.defaultCharset()));
        return new BasicHeader(HttpHeaders.AUTHORIZATION,
                               String.format("Basic %s", encoded));
    }

    /**
     * Decode a JWT token, log the subject and expiration date.
     *
     * @param token Token as {@link String}.
     * @return Token as {@link DecodedJWT}.
     */
    private static DecodedJWT decodeToken(final String token)
            throws FutureGatewayException {
        try {
            final DecodedJWT jwt = JWT.decode(token);
            TokenHelper.LOGGER
                    .debug("Decoded JWT token. Subject: {} ExpiresAt: {}",
                           jwt.getSubject(), jwt.getExpiresAt());
            return jwt;
        } catch (final JWTDecodeException e) {
            String message = TokenHelper.RESOURCE_BUNDLE
                    .getString("failed.to.decode.jwt.token.0");
            message = MessageFormat.format(message, token);
            TokenHelper.LOGGER.error(message);
            throw new FutureGatewayException(message, e);
        }
    }

    /**
     * Use {@link ObjectMapper} to parse HTTP response into a valid
     * {@link TokenServiceResponse} object.
     *
     * @param response Response from the Token service.
     * @return An object representing parsed JSON data.
     * @throws IOException If the mapping causes errors.
     */
    private static TokenServiceResponse readToken(final HttpResponse response)
            throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            response.getEntity().writeTo(outputStream);
            final String body =
                    outputStream.toString(Charset.defaultCharset().name());
            TokenHelper.LOGGER.trace("Body: {}", body); //NON-NLS
            return TokenHelper.MAPPER
                    .readValue(body, TokenServiceResponse.class);
        }
    }
}
