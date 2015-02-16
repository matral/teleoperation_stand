package Leap;

import Maestro.PololuConnector;
import Maestro.ServosPositionsCalulations;
import Maestro.Servo;
import Maestro.ServoBoundaries;
import Maestro.ServosInitialValues;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import controller.JFrameWindow;

public class LeapListener extends Listener {

	private int last_armLeftRightRescaled, last_armUpDownRescaled,
			last_armForwardBackwardRescaled, last_handPitchRescaled,
			last_handRollRescaled, last_handDistanceOfFingersRescaled;

	private double epsilon;

	public void onConnect(Controller controllerP) {
		System.out.println("Connected");
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

		controllerP.enableGesture(Gesture.Type.TYPE_SWIPE);
	}

	public void onDisconnect(Controller controller) {
		System.out.println("Disconnected");
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		if (!frame.hands().isEmpty()) {
			// Get the first hand
			Hand hand = frame.hands().get(0);

			// Check if the hand has any fingers
			FingerList fingers = hand.fingers();
			if (!fingers.isEmpty()) {
				// Calculate the hand's average finger tip position
				Vector avgPos = Vector.zero();
				for (Finger finger : fingers) {
					avgPos = avgPos.plus(finger.tipPosition());
				}

				avgPos = avgPos.divide(fingers.count());

			}
			// Get the hand's sphere radius and palm position
			System.out.println("Hand sphere radius: " + hand.sphereCenter());
			System.out.println("left-right: " + hand.palmPosition().getX()
					+ "up-down: " + hand.palmPosition().getY()
					+ "forward-backward: " + -hand.palmPosition().getZ());

			// Get the hand's normal vector and direction
			Vector normal = hand.palmNormal();
			Vector direction = hand.direction();

			// Calculate the hand's pitch, roll, and yaw angles
			System.out.println("Hand pitch: " + direction.pitch()
					+ " degrees, " + "roll: " + normal.roll() + " degrees, "
					+ "yaw: " + direction.yaw() + " degrees\n");

			if (!fingers.isEmpty()) {

				Vector avgPos = Vector.zero();
				int fingersCount = 2;
				for (int i = 2; i < 4; i++) {
					avgPos = avgPos.plus(fingers.get(2).direction());
					avgPos = avgPos.plus(fingers.get(3).direction());
				}

				avgPos = avgPos.divide(fingersCount);
				
				setServos(
						hand.palmPosition().getX(),
						hand.palmPosition().getY(),
						hand.palmPosition().getZ(),
						direction.pitch(),
						normal.roll(),
						avgPos.getY(),
						fingers.get(2).tipPosition()
								.distanceTo(fingers.get(3).tipPosition()));
			}
		}

	}

	public void setServos(float armLeftRight, float armUpDown,
			float armForwardBackward, double handPitch, double handRoll,
			double fingerTipsY, double distanceOfFingers) {
		// Min Values must be smaller than Max values
		boolean revert = true;
		System.out.println("finger distance : " +distanceOfFingers);
		int armLeftRightRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armLeftRight,
						LeapMinMaxes.ARM_LEFT_RIGHT.getAttributeMinValue(),
						LeapMinMaxes.ARM_LEFT_RIGHT.getAttributeMaxValue(),
						ServoBoundaries.BOTTOM.getServoMinValue(),
						ServoBoundaries.BOTTOM.getServoMaxValue(),
						last_armLeftRightRescaled,
						ServoBoundaries.BOTTOM.getServoEpsilon(),revert);

		int armUpDownRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armUpDown,
						LeapMinMaxes.ARM_UP_DOWN.getAttributeMinValue(),
						LeapMinMaxes.ARM_UP_DOWN.getAttributeMaxValue(),
						ServoBoundaries.DOF_1.getServoMinValue(),
						ServoBoundaries.DOF_1.getServoMaxValue(),
						last_armUpDownRescaled,
						ServoBoundaries.DOF_1.getServoEpsilon(),revert);

		int armForwardBackwardRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armForwardBackward,
						LeapMinMaxes.ARM_FRONT_BACK.getAttributeMinValue(),
						LeapMinMaxes.ARM_FRONT_BACK.getAttributeMaxValue(),
						ServoBoundaries.DOF_2.getServoMinValue(),
						ServoBoundaries.DOF_2.getServoMaxValue(),
						last_armForwardBackwardRescaled,
						ServoBoundaries.DOF_2.getServoEpsilon());
		int handTipYRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(fingerTipsY,
						LeapMinMaxes.TIPS_Y.getAttributeMinValue(),
						LeapMinMaxes.TIPS_Y.getAttributeMaxValue(),
						ServoBoundaries.DOF_3.getServoMinValue(),
						ServoBoundaries.DOF_3.getServoMaxValue(),
						last_handPitchRescaled,
						ServoBoundaries.DOF_3.getServoEpsilon());
		int handRollRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handRoll,
						LeapMinMaxes.ROLL.getAttributeMinValue(),
						LeapMinMaxes.ROLL.getAttributeMaxValue(),
						ServoBoundaries.CATCHER_ROTATOR.getServoMinValue(),
						ServoBoundaries.CATCHER_ROTATOR.getServoMaxValue(),
						last_handRollRescaled,
						ServoBoundaries.CATCHER_ROTATOR.getServoEpsilon());
		int handDistanceOfFingersRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(distanceOfFingers,
						LeapMinMaxes.FINGERS_DISTANCE.getAttributeMinValue(),
						LeapMinMaxes.FINGERS_DISTANCE.getAttributeMaxValue(),
						ServoBoundaries.CATCHER.getServoMinValue(),
						ServoBoundaries.CATCHER.getServoMaxValue(),
						last_handDistanceOfFingersRescaled,
						ServoBoundaries.CATCHER.getServoEpsilon());
		PololuConnector.setTarget(armLeftRightRescaled,
				Servo.BOTTOM.getServoPort());
		PololuConnector
				.setTarget(armUpDownRescaled, Servo.DOF_1.getServoPort());
		PololuConnector.setTarget(armForwardBackwardRescaled,
				Servo.DOF_2.getServoPort());
		PololuConnector.setTarget(handTipYRescaled, Servo.DOF_3.getServoPort());
		PololuConnector.setTarget(handRollRescaled,
				Servo.CATCHER_ROTATOR.getServoPort());
		PololuConnector.setTarget(handDistanceOfFingersRescaled,
				Servo.CATCHER.getServoPort());

	}
}