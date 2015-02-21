package Leap;

public enum StaticLeapMinMaxes {
	ARM_UP_DOWN(170, 300,250,250), ARM_LEFT_RIGHT(-150, 200, 30, 30), ARM_FRONT_BACK(-80, 80, 0, 0), YAW(
			-0.35, 0.28, 0,0), PITCH(-0.7, 0.25,0,0), ROLL(-0.5, 0.7,0,0), FINGERS_DISTANCE(21.5,47,30,30),TIPS_Y(-0.8,0.1,-0.5,-0.5 );

	private double minValue;
	private double maxValue;
	private double minHardcoredValue;
	private double maxHardcoredValue;

	private StaticLeapMinMaxes(double minHardcoredValueP, double maxHardcoredValueP,double minValueP, double maxValueP ) {
		minValue = minValueP;
		maxValue = maxValueP;
		minHardcoredValue = minHardcoredValueP;
		maxHardcoredValue = maxHardcoredValueP;
	}

	public double getAttributeMinValue() {
		return minValue;
	}

	public double getAttributeMaxValue() {
		return maxValue;
	}
	
	public double getHardcoredMinValue() {
		return minHardcoredValue;
	}

	public double getHardcoredMaxValue() {
		return maxHardcoredValue;
	}

}
