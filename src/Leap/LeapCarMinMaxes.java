package Leap;

public enum LeapCarMinMaxes {
	ARM_UP_DOWN(170, 300), ARM_LEFT_RIGHT(-150, 200), ARM_FRONT_BACK(-80, 80), YAW(
			-0.35, 0.28), PITCH(-0.7, 0.25), ROLL(-0.5, 0.7), FINGERS_DISTANCE(21.5,47),TIPS_Y(-0.8,0.1);

	private double minValue;
	private double maxValue;

	private LeapCarMinMaxes(double minValueP, double maxValueP) {
		minValue = minValueP;
		maxValue = maxValueP;
	}

	public double getAttributeMinValue() {
		return minValue;
	}

	public double getAttributeMaxValue() {
		return maxValue;
	}
	
	

}
