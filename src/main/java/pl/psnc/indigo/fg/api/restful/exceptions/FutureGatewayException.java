package pl.psnc.indigo.fg.api.restful.exceptions;

/**
 * Thrown when an error was encountered during communication with Future
 * Gateway.
 */
public class FutureGatewayException extends Exception {
    /**
     * Construct an instance with description of the problem.
     *
     * @param s Problem description.
     */
    public FutureGatewayException(final String s) {
        super(s);
    }

    /**
     * Construct an instance with description and cause of the problem.
     *
     * @param s         Problem description.
     * @param throwable Cause of the problem.
     */
    public FutureGatewayException(final String s, final Throwable throwable) {
        super(s, throwable);
    }
}
