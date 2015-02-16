package Maestro;

public enum Servo {
	//BOTTOM(0), DOF_1(1), DOF_2(2), DOF_3(3), CATCHER_ROTATOR(5), CATCHER(4);
	BOTTOM(5), DOF_1(4), DOF_2(0), DOF_3(3), CATCHER(2),CATCHER_ROTATOR(1);
	private int servoPort;

	private Servo(int port) {
		servoPort = port;
	}

	public int getServoPort() {
		return servoPort;
	}

	
}
