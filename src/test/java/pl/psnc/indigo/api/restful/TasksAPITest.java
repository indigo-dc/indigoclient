package pl.psnc.indigo.api.restful;

import junit.framework.Assert;
import org.junit.Test;
import pl.psnc.indigo.api.restful.TasksAPI;
import pl.psnc.indigo.api.restful.BaseAPI;

public class TasksAPITest {

	@Test
	public void testPrepareTask() {
		TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
		String result = api.prepareTask( "brunor", "1", "hello" );				
	}
}
