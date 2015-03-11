package Leap;

public enum LeapCarMinMaxes {
	ARM_UP_DOWN(170, 300), ARM_LEFT_RIGHT(70, 170), ARM_FRONT_BACK(-40, 80), YAW(
			-1.50, 1.50), PITCH(-0.40, 0.40), ROLL(-1.70, 1.70), FINGERS_DISTANCE(21.5,47),TIPS_Y(-0.8,0.1);

	private double minValue;
	private double maxValue;

	private LeapCarMinMaxes(double minValueP, double maxValueP) {
		minValue = minValueP;
		maxValue = maxValueP;
	}

	public void setAttributeMinMaxValue(double valueP) {

		double adder = Math.abs(((maxValue - minValue) / 2.0));
		minValue = valueP - adder;
		
		maxValue = valueP + adder;
		
	}

	public double getAttributeMinValue() {
		return minValue;
	}

	public double getAttributeMaxValue() {
		return maxValue;
	}
	
	

}
