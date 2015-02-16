package com.thalmic.myo.util;

public enum MyoMinMaxes {
	ARM_UP_DOWN(8.9, 10.7), ARM_LEFT_RIGHT(8.22, 9.5), ARM_FRONT_BACK(-80, 80), YAW(
			-0.5, 0.4), PITCH(-0.7, 0.25), ROLL(-0.5, 0.7), FINGERS_DISTANCE(24,36),TIPS_Y(-1.0,0.1 );

	private double minValue;
	private double maxValue;

	private MyoMinMaxes(double minValueP, double maxValueP) {
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
