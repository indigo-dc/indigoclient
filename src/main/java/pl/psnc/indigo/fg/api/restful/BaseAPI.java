package pl.psnc.indigo.fg.api.restful;

public class BaseAPI {

	public BaseAPI(String httpAddress) {

		this.httpAddress = httpAddress;

	}

	public static String DEFAULT_ADDRESS = "http://90.147.74.77:8888";
	public static String LOCALHOST_ADDRESS = "http://localhost:8888";

	protected String httpAddress = null;

}
