package Maestro;

public class ServosPositionsCalulations {

	public static int rescaleAndCheckEpsilonEcxeeding(
			double deviceValueProposition, double deviceMin, double deviceMax,
			double servoMin, double servoMax, double oldRescaledValue,
			double epsilon, boolean revert) {

		double rescaledValue = rescaleValueFromOldMinMax(
				deviceValueProposition, deviceMin, deviceMax, servoMin,
				servoMax);
		rescaledValue = servoMax + servoMin - rescaledValue;
		if (isServoValueToBeChanged(rescaledValue, oldRescaledValue, epsilon,
				servoMin, servoMax)) {
			return ((int) rescaledValue);
		} else {
			return ((int) oldRescaledValue);
		}
	}
		
	public static int rescaleAndCheckEpsilonEcxeeding(
			double deviceValueProposition, double deviceMin, double deviceMax,
			double servoMin, double servoMax, double oldRescaledValue,
			double epsilon) {

		double rescaledValue = rescaleValueFromOldMinMax(
				deviceValueProposition, deviceMin, deviceMax, servoMin,
				servoMax);
		if (isServoValueToBeChanged(rescaledValue, oldRescaledValue, epsilon,
				servoMin, servoMax)) {
			return ((int) rescaledValue);
		} else {
			return ((int) oldRescaledValue);
		}

	}

	public static double rescaleValueFromOldMinMax(
			double deviceValueProposition, double deviceMin, double deviceMax,
			double servoMin, double servoMax) {
		double oldRange = deviceMax - deviceMin;
		double newRange = servoMax - servoMin;
		double newValueRescaled = (((deviceValueProposition - deviceMin) * newRange) / oldRange)
				+ servoMin;

		if (newValueRescaled > servoMax) {
			return servoMax;
		} else if (newValueRescaled < servoMin) {
			return servoMin;
		} else {
			return newValueRescaled;
		}

	}

	private static boolean isServoValueToBeChanged(double rescaledValue,
			double oldRescaledValue, double epsilon, double servoMin,
			double servoMax) {

		if (countedPercentageDifference(rescaledValue, oldRescaledValue,
				servoMin, servoMax) > epsilon) {
			return true;
		} else {
			return false;
		}
	}

	private static double countedPercentageDifference(double rescaledValue,
			double oldRescaledValue, double servoMin,
			double servoMax){
		
		
		return Math.abs(rescaledValue-oldRescaledValue/(servoMax - servoMin));
	}
}
