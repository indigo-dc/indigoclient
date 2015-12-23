package pl.psnc.indigo.fg.api.restful;

import junit.framework.Assert;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.BaseAPI;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

public class TasksAPITest {

	@Test
	public void testPrepareTask() {
		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
                try {
                    Task result = api.prepareTask( "brunor", "1", "hello" );				
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
	}

	@Test
	public void testSubmitTask() {
		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
                try {
                    Task prepTask = api.prepareTask( "brunor", "1", "hello" );

                    Upload upload = api.submitTask( "brunor", prepTask.getId() );
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
	}

	@Test
	public void testGetTask() {
		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
	
		// TODO: make sure to set proper task ID below
		// this one is an arbitrary value from previous calls to
		// TaskAPI
                try {
                    Task task = api.getTask("brunor", "44" );
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
	}
        
        @Test
        public void testFinalize() {
            System.out.flush();
        }
}
