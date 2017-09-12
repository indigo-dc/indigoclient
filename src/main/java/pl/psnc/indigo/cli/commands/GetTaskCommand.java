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
  
  String id = null;
  String url = null;
  String token = null;
  
  public GetTaskCommand(
          String id,
          String url,
          String token) {
    
    this.id = id;
    this.url = url;
    this.token = token;
  }
  
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

