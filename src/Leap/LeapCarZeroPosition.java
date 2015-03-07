package Leap;

public enum LeapCarZeroPosition {
	ARM_LEFT_RIGHT(100), ARM_FRONT_BACK(0), YAW(0);

	private double position;

	private LeapCarZeroPosition(double positionP) {
		position = positionP;
	}

	public double getStaticMinValue() {
		return position;
	}


	public void setStaticMinValue(double positionP) {
		position = positionP;
	}


}
