package pl.psnc.indigo.cli.commands;

import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;

/**
 * This interface represents <i>Abstract Command</i> that can
 * can be implemented by specific targets.
 *
 * @author Michal K. Owsiak
 */

public interface AbstractCommand {
    /**
     * Executes command can return two different values.
     *
     * @return 0    - everything is OK
     * != 0 - something is definitelly not right
     * @throws FutureGatewayException When communication with FutureGateway
     *                                fails.
     */
    int execute() throws FutureGatewayException;
}
