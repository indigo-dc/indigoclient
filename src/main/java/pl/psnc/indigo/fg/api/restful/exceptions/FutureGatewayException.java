package pl.psnc.indigo.fg.api.restful.exceptions;

/**
 * Created by tzok on 20.05.16.
 */
public class FutureGatewayException extends Exception {
    public FutureGatewayException(String s) {
        super(s);
    }

    public FutureGatewayException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
