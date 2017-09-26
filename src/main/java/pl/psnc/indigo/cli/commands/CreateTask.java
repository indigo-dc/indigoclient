/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.commands;

import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for creating Task at FG API server.
 *
 * @author michalo
 */
public final class CreateTask implements AbstractCommand {
    private final String appName;
    private final String description;
    private final List<String> arguments;
    private final List<String> inputFileNames;
    private final List<String> outputFileNames;
    private final String url;
    private final String token;

    /**
     * Parametric constructor for the command.
     *
     * @param appName         Name of the application to be created
     * @param description     Description of the application
     * @param arguments       Arguments passed to the application
     * @param inputFileNames  List of input file names
     * @param outputFileNames List of output file names
     * @param url             URL of FG API server
     * @param token           User's token
     */
    public CreateTask(final String appName, final String description,
                      final List<String> arguments,
                      final List<String> inputFileNames,
                      final List<String> outputFileNames, final String url,
                      final String token) {
        super();

        this.appName = appName;
        this.description = description;
        this.arguments = new ArrayList<>(arguments);
        this.inputFileNames = new ArrayList<>(inputFileNames);
        this.outputFileNames = new ArrayList<>(outputFileNames);
        this.url = url;
        this.token = token;
    }

    /**
     * Executes command can return two different values.
     *
     * @return 0 - everything is OK != 0 - something is definitely not right
     * @throws FutureGatewayException When task creation fails.
     */
    public int execute() throws FutureGatewayException {
        Task task = new Task();
        task.setDescription(description);
        task.setApplication(appName);
        task.setArguments(arguments);

        // we have to convert file names to InputFile objects
        final int inputSize = inputFileNames.size();
        final int outputSize = outputFileNames.size();

        final List<InputFile> inputFiles = new ArrayList<>(inputSize);
        for (final String fileName : inputFileNames) {
            final InputFile inputFile = new InputFile();
            inputFile.setName(fileName);
            inputFiles.add(inputFile);
        }

        final List<OutputFile> outputFiles = new ArrayList<>(outputSize);
        for (final String fileName : outputFileNames) {
            final OutputFile outputFile = new OutputFile();
            outputFile.setName(fileName);
            outputFiles.add(outputFile);
        }

        task.setInputFiles(inputFiles);
        task.setOutputFiles(outputFiles);

        final TasksAPI api = new TasksAPI(URI.create(url), token);
        task = api.createTask(task);

        final String id = task.getId();
        System.out.println("Task ID: " + id);

        return 0;
    }
}



