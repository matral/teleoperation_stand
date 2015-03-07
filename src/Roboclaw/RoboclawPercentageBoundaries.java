package Roboclaw;

public enum RoboclawPercentageBoundaries {
	LEFT_RIGHT(0,100,0.06), FORWARD_BACKWARD(0,100,0.06), YAW_LEFT_RIGHT(0,100,0.06);

	private int minValue;
	private int maxValue;
	private double epsilon;

	private RoboclawPercentageBoundaries(int minValueP, int maxValueP, double epsilonP) {
		minValue = minValueP;
		maxValue = maxValueP;
		epsilon = epsilonP;
	}

	public int getServoMinValue() {
		return minValue;
	}
	public int getServoMaxValue() {
		return maxValue;
	}
	public double getServoEpsilon() {
		return epsilon;
	}
	
}
