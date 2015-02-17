package Maestro;

public enum ServoBoundaries {
	BOTTOM(0,90,0.06), DOF_1(0,200,0.06), DOF_2(0,90,0.06), DOF_3(0,90,0.06), CATCHER_ROTATOR(0,90,0.06), CATCHER(0,90,0.06);
	//BOTTOM(1010,1980,0.06), DOF_1(900,1980,0.06), DOF_2(1010,1980,0.06), DOF_3(1010,1980,0.06), CATCHER_ROTATOR(1010,1980,0.06), CATCHER(1010,1980,0.06);
	//BOTTOM(515,2470,0.5), DOF_1(520,2240,0.1),DOF_2(1010,1980,0.1),DOF_3(520,2435,0.075), CATCHER(1480,1950,0.05), CATCHER_ROTATOR(1010,1980,0.1);
	
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
