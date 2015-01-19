package controller.util;

import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;

public class ConnectionBuilder {
	private AmberClient amberClient;
	private RoboclawProxy roboclawProxy;
	
	public AmberClient getAmberClient() {
		return amberClient;
	}

	public RoboclawProxy getRoboclawProxy() {
		return roboclawProxy;
	}
	
	public ConnectionBuilder (AmberClient client,
			RoboclawProxy proxy) {
		amberClient = client;
		roboclawProxy = proxy;
	}
}
