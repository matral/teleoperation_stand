package com.thalmic.myo.example;

import Maestro.PololuConnector;
import Maestro.Servo;
import Maestro.ServoBoundaries;
import Maestro.ServosInitialValues;
import Maestro.ServosPositionsCalulations;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.enums.Arm;
import com.thalmic.myo.enums.PoseType;
import com.thalmic.myo.enums.VibrationType;
import com.thalmic.myo.enums.XDirection;
import com.thalmic.myo.util.MyoMinMaxes;

public class DataCollector extends AbstractDeviceListener {
	private static final int SCALE = 18;
	private double rollW;
	private double pitchW;
	private double yawW;
	private Pose currentPose;
	private Arm whichArm;

	private int last_armLeftRightRescaled, last_armUpDownRescaled,
			last_armForwardBackwardRescaled, last_handPitchRescaled,
			last_handRollRescaled, last_handDistanceOfFingersRescaled;
	private double epsilon;

	public DataCollector() {
		rollW = 0;
		pitchW = 0;
		yawW = 0;
		currentPose = new Pose();
		last_armLeftRightRescaled = ServosInitialValues.BOTTOM
				.getInitialPosition();
		last_armUpDownRescaled = ServosInitialValues.DOF_1.getInitialPosition();
		last_armForwardBackwardRescaled = ServosInitialValues.DOF_2
				.getInitialPosition();
		last_handPitchRescaled = ServosInitialValues.DOF_3.getInitialPosition();
		last_handRollRescaled = ServosInitialValues.CATCHER_ROTATOR
				.getInitialPosition();
		last_handDistanceOfFingersRescaled = ServosInitialValues.CATCHER
				.getInitialPosition();
		// Percentage Value
		epsilon = 0.12;

	}

	@Override
	public void onGyroscopeData(Myo myo, long timestamp,
			com.thalmic.myo.Vector3 gyro) {
		//System.out.println("gyroscope" + gyro.normalized());
	};

	@Override
	public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
		Quaternion normalized = rotation.normalized();
		//System.out.println("w : " + normalized.getW());

		double roll = Math
				.atan2(2.0f * (normalized.getW() * normalized.getX() + normalized
						.getY() * normalized.getZ()),
						1.0f - 2.0f * (normalized.getX() * normalized.getX() + normalized
								.getY() * normalized.getY()));
		double pitch = Math
				.asin(2.0f * (normalized.getW() * normalized.getY() - normalized
						.getZ() * normalized.getX()));
		double yaw = Math
				.atan2(2.0f * (normalized.getW() * normalized.getZ() + normalized
						.getX() * normalized.getY()),
						1.0f - 2.0f * (normalized.getY() * normalized.getY() + normalized
								.getZ() * normalized.getZ()));

		rollW = ((roll + Math.PI) / (Math.PI * 2.0) * SCALE);
		pitchW = ((pitch + Math.PI / 2.0) / Math.PI * SCALE);
		yawW = ((yaw + Math.PI) / (Math.PI * 2.0) * SCALE);
		System.out.println(rollW + ", " + pitchW );
		
		setServos(normalized.getX(), normalized.getY(), normalized.getZ(), pitchW, pitchW, pitchW, pitchW);
	}
	public void setServos(double armLeftRight, double armUpDown,
			double armForwardBackward, double handPitch, double handRoll,
			double fingerTipsY, double distanceOfFingers) {
		// Min Values must be smaller than Max values
		boolean revert = true;
		int armLeftRightRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armLeftRight,
						MyoMinMaxes.ARM_LEFT_RIGHT.getAttributeMinValue(),
						MyoMinMaxes.ARM_LEFT_RIGHT.getAttributeMaxValue(),
						ServoBoundaries.BOTTOM.getServoMinValue(),
						ServoBoundaries.BOTTOM.getServoMaxValue(),
						last_armLeftRightRescaled,
						ServoBoundaries.BOTTOM.getServoEpsilon(), revert);

		int armUpDownRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armUpDown,
						MyoMinMaxes.ARM_UP_DOWN.getAttributeMinValue(),
						MyoMinMaxes.ARM_UP_DOWN.getAttributeMaxValue(),
						ServoBoundaries.DOF_1.getServoMinValue(),
						ServoBoundaries.DOF_1.getServoMaxValue(),
						last_armUpDownRescaled,
						ServoBoundaries.DOF_1.getServoEpsilon(), revert);


		// int armForwardBackwardRescaled =
		// RescaleToServoMinMax.rescaleAndCheckEpsilonEcxeeding(
		// armForwardBackward,
		// MyoMinMaxes.ARM_FRONT_BACK.getAttributeMinValue(),
		// MyoMinMaxes.ARM_FRONT_BACK.getAttributeMaxValue(),
		// ServoBoundaries.DOF_2.getServoMinValue(),
		// ServoBoundaries.DOF_2.getServoMaxValue(),
		// last_armForwardBackwardRescaled,
		// ServoBoundaries.DOF_2.getServoEpsilon);
		int handTipYRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(fingerTipsY,
						MyoMinMaxes.TIPS_Y.getAttributeMinValue(),
						MyoMinMaxes.TIPS_Y.getAttributeMaxValue(),
						ServoBoundaries.DOF_3.getServoMinValue(),
						ServoBoundaries.DOF_3.getServoMaxValue(),
						last_handPitchRescaled,
						ServoBoundaries.DOF_3.getServoEpsilon());
		// int handRollRescaled =
		// RescaleToServoMinMax.rescaleAndCheckEpsilonEcxeeding(
		// handRoll,
		// LeapMinMaxes.ROLL.getAttributeMinValue(),
		// LeapMinMaxes.ROLL.getAttributeMaxValue(),
		// ServoBoundaries.CATCHER_ROTATOR.getServoMinValue(),
		// ServoBoundaries.CATCHER_ROTATOR.getServoMaxValue(),
		// last_handRollRescaled,
		// ServoBoundaries.CATCHER_ROTATOR.getServoEpsilon());
		int handDistanceOfFingersRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(distanceOfFingers,
						MyoMinMaxes.FINGERS_DISTANCE.getAttributeMinValue(),
						MyoMinMaxes.FINGERS_DISTANCE.getAttributeMaxValue(),
						ServoBoundaries.CATCHER.getServoMinValue(),
						ServoBoundaries.CATCHER.getServoMaxValue(),
						last_handDistanceOfFingersRescaled,
						ServoBoundaries.CATCHER.getServoEpsilon());
		System.out.println(armLeftRightRescaled + ", " + armUpDownRescaled );
		PololuConnector.setTarget(armLeftRightRescaled,
				Servo.BOTTOM.getServoPort());
		PololuConnector
				.setTarget(armUpDownRescaled, Servo.DOF_1.getServoPort());
		// /PololuConnector.setTarget(armForwardBackwardRescaled,
		// Servo.DOF_2.getServoPort());
		//PololuConnector.setTarget(handTipYRescaled, Servo.DOF_3.getServoPort());
		// /PololuConnector.setTarget(handRollRescaled,
		// Servo.CATCHER_ROTATOR.getServoPort());
		//PololuConnector.setTarget(handDistanceOfFingersRescaled,
		//		Servo.CATCHER.getServoPort());}
	}
	@Override
	public void onPose(Myo myo, long timestamp, Pose pose) {
		currentPose = pose;
		if (currentPose.getType() == PoseType.FIST) {
			myo.vibrate(VibrationType.VIBRATION_MEDIUM);
		}
	}

	@Override
	public void onArmSync(Myo myo, long timestamp, Arm arm,
			XDirection xDirection) {
		whichArm = arm;
	}

	@Override
	public void onArmUnsync(Myo myo, long timestamp) {
		whichArm = null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("\r");

		String xDisplay = String.format("[%s%s]",
				repeatCharacter('*', (int) rollW),
				repeatCharacter(' ', (int) (SCALE - rollW)));
		String yDisplay = String.format("[%s%s]",
				repeatCharacter('*', (int) pitchW),
				repeatCharacter(' ', (int) (SCALE - pitchW)));
		String zDisplay = String.format("[%s%s]",
				repeatCharacter('*', (int) yawW),
				repeatCharacter(' ', (int) (SCALE - yawW)));

		String armString = null;
		if (whichArm != null) {
			armString = String.format("[%s]", whichArm == Arm.ARM_LEFT ? "L"
					: "R");
		} else {
			armString = String.format("[?]");
		}
		String poseString = null;
		if (currentPose != null) {
			String poseTypeString = currentPose.getType().toString();
			poseString = String.format(
					"[%s%" + (SCALE - poseTypeString.length()) + "s]",
					poseTypeString, " ");
		} else {
			poseString = String.format("[%14s]", " ");
		}
		builder.append(xDisplay);
		builder.append(yDisplay);
		builder.append(zDisplay);
		builder.append(armString);
		builder.append(poseString);
		return builder.toString();

	}

	private String repeatCharacter(char character, int numOfTimes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < numOfTimes; i++) {
			builder.append(character);
		}
		return builder.toString();
	}
}