package pl.psnc.indigo.api.restful;

import junit.framework.Assert;
import org.junit.Test;
import pl.psnc.indigo.api.restful.RootAPI;
import pl.psnc.indigo.api.restful.BaseAPI;

public class RootAPITest {

	@Test
	public void testGetRoot() {
		RootAPI api = new RootAPI(BaseAPI.LOCALHOST_ADDRESS);
		String result = api.getRoot();
		System.out.println("Result: " + result);
	}
}
