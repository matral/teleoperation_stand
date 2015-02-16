package controller.util;

public enum ActualArmControllerDevice {
	KEYBOARD(0), LEAP(1),MYO(2),PHANTOM(3);

	private double index;

	private ActualArmControllerDevice(double indexP) {
		index = indexP;
	}

	public double getIndex() {
		return index;
	}


}
