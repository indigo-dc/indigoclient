package pl.psnc.indigo.fg.api.restful.exceptions;

public class FutureGatewayException extends Exception {
    public FutureGatewayException(String s) {
        super(s);
    }

    public FutureGatewayException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
