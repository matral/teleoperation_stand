package Maestro;

public enum ServosInitialValues {
	//BOTTOM(1340,20,20), DOF_1(1760,20,20), DOF_2(1420,20,20), DOF_3(1036,20,20), CATCHER(1640,20,20), CATCHER_ROTATOR(1200,20,20);
	BOTTOM(1340,60,60), DOF_1(894,60,60), DOF_2(1200,60,60), DOF_3(700,60,60), CATCHER(1640,60,60), CATCHER_ROTATOR(1200,60,60);
	private int initialPosition;
	private int speed;
	private int acceleration;

	private ServosInitialValues(int initialPositionP, int initialSpeedP, int initialAccelerationP) {
		initialPosition = initialPositionP;
		speed = initialSpeedP;
		acceleration = initialAccelerationP;
	}

	public int getInitialPosition() {
		return initialPosition;
	}
	public int getSpeed() {
		return speed;
		
	}
	public int getAcceleration() {
		return acceleration;
	}

	
}
