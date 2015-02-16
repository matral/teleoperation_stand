package Maestro;

public enum ServoBoundaries {
	//BOTTOM(1010,1980,0.1), DOF_1(900,1980,0.12), DOF_2(1010,1980,0.12), DOF_3(1010,1980,0.12), CATCHER_ROTATOR(1010,1980,0.12), CATCHER(1010,1980,0.05);
	BOTTOM(515,2470,0.1), DOF_1(520,2240,0.12),DOF_2(1010,1980,0.12),DOF_3(520,2435,0.12), CATCHER(1480,1950,0.05), CATCHER_ROTATOR(1010,1980,0.12);
	
	private int minValue;
	private int maxValue;
	private double epsilon;

	private ServoBoundaries(int minValueP, int maxValueP, double epsilonP) {
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
