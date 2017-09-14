/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.commands;

import java.net.URI;
import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;

/**
 *
 * @author michalo
 */
public class GetTaskCommand implements AbstractCommand {

  private String id = null;
  private String url = null;
  private String token = null;

  /**
   * Creates command for getting information about task with given ID.
   * @param id ID of the task
   * @param url URL of FG API server
   * @param token User's token
   */
  public GetTaskCommand(
          final String id,
          final String url,
          final String token) {

    this.id = id;
    this.url = url;
    this.token = token;
  }

  /**
   * Executes command that retrieves current status of Task.
   * @return 0    - everything is OK
   *         != 0 - something went wrong
   * @throws Exception There was a serious problem while getting status.
   */
  public final int execute() throws Exception {
    // we have to call stuff
    final TasksAPI api = new TasksAPI(URI.create(url), token);
    final Task task = api.getTask(id);
    System.out.println(
            "Task ID: " + task.getId() + "\n" +
            "Name: " + task.getApplication() + "\n" +
            "Status: " + task.getStatus() + "\n" +
            "Description: " + task.getDescription() + "\n");
    return 0;
  }

}

