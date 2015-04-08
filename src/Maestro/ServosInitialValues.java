package Maestro;

public enum ServosInitialValues {
	BOTTOM(90,150,20), DOF_1(130,150,20), DOF_2(90,150,20), DOF_3(90,150,20), CATCHER(90,150,20), CATCHER_ROTATOR(90,150,20); //panda
	//BOTTOM(1340,13,20), DOF_1(1760,13,20), DOF_2(1420,13,20), DOF_3(1036,13,20), CATCHER(1640,13,20), CATCHER_ROTATOR(1200,13,20); //lab
	//BOTTOM(1340,60,60), DOF_1(894,60,60), DOF_2(1200,60,60), DOF_3(700,60,60), CATCHER(1640,60,60), CATCHER_ROTATOR(1200,60,60); //home
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
