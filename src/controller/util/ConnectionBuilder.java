package controller.util;

import pl.edu.agh.amber.common.AmberClient;
import src.main.java.pl.edu.agh.amber.hitec.*;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;

public class ConnectionBuilder {
	private AmberClient amberClient;
	private RoboclawProxy roboclawProxy;
	private HitecProxy hitecProxy;
	private com.leapmotion.leap.Controller leapController;
	
	public AmberClient getAmberClient() {
		return amberClient;
	}

	public RoboclawProxy getRoboclawProxy() {
		return roboclawProxy;
	}
	
	public HitecProxy getHitecProxy() {
		return hitecProxy;
	}
	public com.leapmotion.leap.Controller getLeapController(){
		return new com.leapmotion.leap.Controller();
	}
	
	
	public ConnectionBuilder (AmberClient client,
			RoboclawProxy roboclawProx, HitecProxy hitecProx) {
		amberClient = client;
		roboclawProxy = roboclawProx;
		hitecProxy = hitecProx;
	}
	
	public ConnectionBuilder (AmberClient client,
			RoboclawProxy roboclawProx, HitecProxy hitecProx, com.leapmotion.leap.Controller leapMotionController) {
		amberClient = client;
		roboclawProxy = roboclawProx;
		hitecProxy = hitecProx;
		leapController = leapMotionController;
	}
}
