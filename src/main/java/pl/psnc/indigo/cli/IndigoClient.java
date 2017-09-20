package pl.psnc.indigo.cli;

import org.apache.commons.cli.ParseException;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.parser.ICParser;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;

/**
 * IndigoClient main class.
 */
public final class IndigoClient {
    /**
     * IndigoClient main entry point.
     *
     * @param args Arguments of application
     * @throws ParseException         When parsing fails.
     * @throws FutureGatewayException When communication with FutureGateway
     * fails.
     */
    public static void main(final String[] args)
            throws ParseException, FutureGatewayException {
        final ICParser parser = new ICParser(args);
        for (final AbstractCommand cmd : parser.parse()) {
            cmd.execute();
        }
    }

    /**
     * Just to make sure it will not be created somewhere.
     */
    private IndigoClient() {
        super();
    }
}
