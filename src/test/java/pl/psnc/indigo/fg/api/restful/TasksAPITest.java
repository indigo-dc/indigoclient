package pl.psnc.indigo.fg.api.restful;

import junit.framework.Assert;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.BaseAPI;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

public class TasksAPITest {

	@Test
	public void testCreateTask() {
		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
                try {
                    Task newTask = new Task();
                    newTask.setUser("brunor");
                    newTask.setApplication("1");
                    newTask.setDescription("hello");
                    Task result = api.createTask( newTask );				
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
	}

//	@Test
//	public void testSubmitTask() {
//		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
//                try {
//                    Task newTask = new Task();
//                    newTask.setUser("brunor");
//                    newTask.setApplication("1");
//                    newTask.setDescription("hello");
//                    Task prepTask = api.createTask( newTask );
//
//                    Upload upload = api.submitTask( prepTask );
//                } catch(Exception ex) {
//                    ex.printStackTrace();
//                }
//	}

	@Test
	public void testGetTask() {
		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
	
		// TODO: make sure to set proper task ID below
		// this one is an arbitrary value from previous calls to
		// TaskAPI
                try {
                    Task newTask = new Task();
                    newTask.setUser("brunor");
                    newTask.setId("27");
                    
                    Task task = api.getTask( newTask );
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
	}
        
        @Test
	public void testGetAllTasks() {
		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
	
		// TODO: make sure to set proper task ID below
		// this one is an arbitrary value from previous calls to
		// TaskAPI
                try {
                    Task [] tasks = api.getAllTasks();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
	}
        
        @Test
        public void testFinalize() {
            System.out.flush();
        }
}
