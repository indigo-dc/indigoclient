package pl.psnc.indigo.cli.commands;

import org.apache.commons.cli.HelpFormatter;
import pl.psnc.indigo.cli.OptionComparator;

/**
 * Class responsible for printing Help of the application.
 */
public class Help implements AbstractCommand {
    /**
     * Execute help command.
     *
     * @return 0    - everything is OK
     * != 0 - something is definitelly not right
     */
    public final int execute() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(OptionComparator.getInstance());
        formatter.printHelp(
                "java -cp ./indigoAPI.jar pl.psnc.indigo.cli.IndigoClient",
                OptionComparator.options());
        return 0;
    }
}
