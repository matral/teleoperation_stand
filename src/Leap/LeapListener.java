package Leap;

import Maestro.PololuConnector;
import Maestro.ServosPositionsCalulations;
import Maestro.Servo;
import Maestro.ServoBoundaries;
import Maestro.ServosInitialValues;
import Roboclaw.RoboclawPercentageBoundaries;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import controller.JFrameWindow;
import controller.util.ConnectionBuilder;

public class LeapListener extends Listener {

	private int last_armLeftRightRescaled, last_armUpDownRescaled,
			last_armForwardBackwardRescaled, last_handPitchRescaled,
			last_handRollRescaled, last_handDistanceOfFingersRescaled,
			last_handTipYRescaled,last_handYawRescaled;

	private double epsilon;

	public void onConnect(Controller controllerP) {
		System.out.println("Connected");
		last_armLeftRightRescaled = ServosInitialValues.BOTTOM
				.getInitialPosition();
		last_armUpDownRescaled = ServosInitialValues.DOF_1.getInitialPosition();
		last_armForwardBackwardRescaled = ServosInitialValues.DOF_2
				.getInitialPosition();
		last_handTipYRescaled = ServosInitialValues.DOF_3.getInitialPosition();
		last_handRollRescaled = ServosInitialValues.CATCHER_ROTATOR
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

				/*
				 * setServos( hand.palmPosition().getX(),
				 * hand.palmPosition().getY(), hand.palmPosition().getZ(),
				 * direction.pitch(), normal.roll(), avgPos.getY(),
				 * fingers.get(2).tipPosition()
				 * .distanceTo(fingers.get(3).tipPosition()));
				 */
			}
		}

	}
	public void setServos(float armLeftRight, float armUpDown,
			float armForwardBackward, double handPitch, double handRoll,
			double fingerTipsY, double distanceOfFingers,
			ConnectionBuilder connection) {
		// Min Values must be smaller than Max values
		boolean revert = true;

		int armLeftRightRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armLeftRight,
						LeapArmMinMaxes.ARM_LEFT_RIGHT.getAttributeMinValue(),
						LeapArmMinMaxes.ARM_LEFT_RIGHT.getAttributeMaxValue(),
						ServoBoundaries.BOTTOM.getServoMinValue(),
						ServoBoundaries.BOTTOM.getServoMaxValue(),
						last_armLeftRightRescaled,
						ServoBoundaries.BOTTOM.getServoEpsilon(),revert);

		int armUpDownRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armUpDown,
						LeapArmMinMaxes.ARM_UP_DOWN.getAttributeMinValue(),
						LeapArmMinMaxes.ARM_UP_DOWN.getAttributeMaxValue(),
						ServoBoundaries.DOF_1.getServoMinValue(),
						ServoBoundaries.DOF_1.getServoMaxValue(),
						last_armUpDownRescaled,
						ServoBoundaries.DOF_1.getServoEpsilon(),revert);

		int armForwardBackwardRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(armForwardBackward,
						LeapArmMinMaxes.ARM_FRONT_BACK.getAttributeMinValue(),
						LeapArmMinMaxes.ARM_FRONT_BACK.getAttributeMaxValue(),
						ServoBoundaries.DOF_2.getServoMinValue(),
						ServoBoundaries.DOF_2.getServoMaxValue(),
						last_armForwardBackwardRescaled,
						ServoBoundaries.DOF_2.getServoEpsilon());
		int handPitchRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handPitch,
						LeapArmMinMaxes.PITCH.getAttributeMinValue(),
						LeapArmMinMaxes.PITCH.getAttributeMaxValue(),
						ServoBoundaries.DOF_3.getServoMinValue(),
						ServoBoundaries.DOF_3.getServoMaxValue(),
						last_handPitchRescaled,
						ServoBoundaries.DOF_3.getServoEpsilon(), revert);
		int handRollRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handRoll,
						LeapArmMinMaxes.ROLL.getAttributeMinValue(),
						LeapArmMinMaxes.ROLL.getAttributeMaxValue(),
						ServoBoundaries.CATCHER_ROTATOR.getServoMinValue(),
						ServoBoundaries.CATCHER_ROTATOR.getServoMaxValue(),
						last_handTipYRescaled,
						ServoBoundaries.CATCHER_ROTATOR.getServoEpsilon(),revert);
		int handDistanceOfFingersRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(distanceOfFingers,
						LeapArmMinMaxes.FINGERS_DISTANCE.getAttributeMinValue(),
						LeapArmMinMaxes.FINGERS_DISTANCE.getAttributeMaxValue(),
						ServoBoundaries.CATCHER.getServoMinValue(),
						ServoBoundaries.CATCHER.getServoMaxValue(),
						last_handDistanceOfFingersRescaled,
						ServoBoundaries.CATCHER.getServoEpsilon(), revert);
		if (last_armLeftRightRescaled != armLeftRightRescaled) {
			/*
			 * PololuConnector.setTarget(armLeftRightRescaled,
			 * Servo.BOTTOM.getServoPort());
			 */
			connection.getHitecProxy().setAngle(Servo.BOTTOM.getServoPort(),
					armLeftRightRescaled);
			last_armLeftRightRescaled = armLeftRightRescaled;
		}
		if (last_armUpDownRescaled != armUpDownRescaled) {
			/*
			 * PololuConnector.setTarget(armUpDownRescaled,
			 * Servo.DOF_1.getServoPort());
			 */
			connection.getHitecProxy().setAngle(Servo.DOF_1.getServoPort(),
					armUpDownRescaled);
			last_armUpDownRescaled = armUpDownRescaled;
		}
		if (last_armForwardBackwardRescaled != armForwardBackward) {
			/*
			 * PololuConnector.setTarget(armForwardBackwardRescaled,
			 * Servo.DOF_2.getServoPort());
			 */
			connection.getHitecProxy().setAngle(Servo.DOF_2.getServoPort(),
					armForwardBackwardRescaled);
			last_armForwardBackwardRescaled = armForwardBackwardRescaled;
		}
		if (last_handPitchRescaled != handPitchRescaled) {
			/*
			 * PololuConnector.setTarget(handPitchRescaled,
			 * Servo.DOF_3.getServoPort());
			 */
			connection.getHitecProxy().setAngle(Servo.DOF_3.getServoPort(),
					handPitchRescaled);
			last_handPitchRescaled = handPitchRescaled;
		}
		if (last_handRollRescaled != handRollRescaled) {
			/*
			 * PololuConnector.setTarget(handRollRescaled,
			 * Servo.CATCHER_ROTATOR.getServoPort());
			 */
			connection.getHitecProxy().setAngle(
					Servo.CATCHER_ROTATOR.getServoPort(), handRollRescaled);
			last_handRollRescaled = handRollRescaled;
		}
		if (last_handDistanceOfFingersRescaled != handDistanceOfFingersRescaled) {
			/*
			 * PololuConnector.setTarget(handDistanceOfFingersRescaled,
			 * Servo.CATCHER.getServoPort());
			 */
			connection.getHitecProxy().setAngle(Servo.CATCHER.getServoPort(),
					handDistanceOfFingersRescaled);
			last_handDistanceOfFingersRescaled = handDistanceOfFingersRescaled;
		}

	}

	public void setLipMinMaxes(float armLeftRight, float armUpDown,
			float armForwardBackward, double handPitch, double handRoll,
			double fingerTipsY, double distanceOfFingers) {
		System.out.println("Adjusting leap min maxes");
		System.out.println("Arm left : "
				+ LeapArmMinMaxes.ARM_LEFT_RIGHT.getAttributeMinValue()
				+ "Arm right : "
				+ LeapArmMinMaxes.ARM_LEFT_RIGHT.getAttributeMaxValue());
		System.out.println("Arm down : "
				+ LeapArmMinMaxes.ARM_UP_DOWN.getAttributeMinValue() + "Arm up : "
				+ LeapArmMinMaxes.ARM_UP_DOWN.getAttributeMaxValue());
		System.out.println("Arm back : "
				+ LeapArmMinMaxes.ARM_FRONT_BACK.getAttributeMinValue()
				+ "Arm front : "
				+ LeapArmMinMaxes.ARM_FRONT_BACK.getAttributeMaxValue());
		System.out.println("Arm pitch min : "
				+ LeapArmMinMaxes.PITCH.getAttributeMinValue()
				+ "Arm pitch max : "
				+ LeapArmMinMaxes.PITCH.getAttributeMaxValue());
		System.out.println("Arm roll min : "
				+ LeapArmMinMaxes.ROLL.getAttributeMinValue() + "Arm roll max : "
				+ LeapArmMinMaxes.ROLL.getAttributeMaxValue());
		System.out.println("Finger distance min : "
				+ LeapArmMinMaxes.FINGERS_DISTANCE.getAttributeMinValue()
				+ "Finger distance max : "
				+ LeapArmMinMaxes.FINGERS_DISTANCE.getAttributeMaxValue());

		if (armLeftRight > LeapArmMinMaxes.ARM_LEFT_RIGHT.getStaticMaxValue()) {
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setAttributeMaxValue(armLeftRight);
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setStaticMaxValue(armLeftRight);
		}
		if (armLeftRight < LeapArmMinMaxes.ARM_LEFT_RIGHT.getStaticMinValue()) {
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setAttributeMinValue(armLeftRight);
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setStaticMinValue(armLeftRight);
		}

		if (armForwardBackward > LeapArmMinMaxes.ARM_FRONT_BACK
				.getStaticMaxValue()) {
			LeapArmMinMaxes.ARM_FRONT_BACK
					.setAttributeMaxValue(armForwardBackward);
			LeapArmMinMaxes.ARM_FRONT_BACK.setStaticMaxValue(armForwardBackward);
		}
		if (armForwardBackward < LeapArmMinMaxes.ARM_FRONT_BACK
				.getStaticMinValue()) {
			LeapArmMinMaxes.ARM_FRONT_BACK
					.setAttributeMinValue(armForwardBackward);
			LeapArmMinMaxes.ARM_FRONT_BACK.setStaticMinValue(armForwardBackward);
		}
		if (armUpDown > LeapArmMinMaxes.ARM_UP_DOWN.getStaticMaxValue()) {
			LeapArmMinMaxes.ARM_UP_DOWN.setAttributeMaxValue(armUpDown);
			LeapArmMinMaxes.ARM_UP_DOWN.setStaticMaxValue(armUpDown);
		}
		if (armUpDown < LeapArmMinMaxes.ARM_UP_DOWN.getStaticMinValue()) {
			LeapArmMinMaxes.ARM_UP_DOWN.setAttributeMinValue(armUpDown);
			LeapArmMinMaxes.ARM_UP_DOWN.setStaticMinValue(armUpDown);
		}
		if (handRoll > LeapArmMinMaxes.ROLL.getStaticMaxValue()) {
			LeapArmMinMaxes.ROLL.setAttributeMaxValue(handRoll);
			LeapArmMinMaxes.ROLL.setStaticMaxValue(handRoll);
		}
		if (handRoll < LeapArmMinMaxes.ROLL.getStaticMinValue()) {
			LeapArmMinMaxes.ROLL.setAttributeMinValue(handRoll);
			LeapArmMinMaxes.ROLL.setStaticMinValue(handRoll);
		}
		if (handPitch > LeapArmMinMaxes.PITCH.getStaticMaxValue()) {
			LeapArmMinMaxes.PITCH.setAttributeMaxValue(handPitch);
			LeapArmMinMaxes.PITCH.setStaticMaxValue(handPitch);
		}
		if (handPitch < LeapArmMinMaxes.PITCH.getStaticMinValue()) {
			LeapArmMinMaxes.PITCH.setAttributeMinValue(handPitch);
			LeapArmMinMaxes.PITCH.setStaticMinValue(handPitch);
		}

		if (distanceOfFingers > LeapArmMinMaxes.FINGERS_DISTANCE
				.getStaticMaxValue()) {
			LeapArmMinMaxes.FINGERS_DISTANCE
					.setAttributeMaxValue(distanceOfFingers);
			LeapArmMinMaxes.FINGERS_DISTANCE.setStaticMaxValue(distanceOfFingers);
		}
		if (distanceOfFingers < LeapArmMinMaxes.FINGERS_DISTANCE
				.getStaticMinValue()) {
			LeapArmMinMaxes.FINGERS_DISTANCE
					.setAttributeMinValue(distanceOfFingers);
			LeapArmMinMaxes.FINGERS_DISTANCE.setStaticMinValue(distanceOfFingers);
		}

	}

	public void setLipMinMaxesStaticValues() {
		LeapArmMinMaxes.ARM_LEFT_RIGHT
				.setStaticMinValue(StaticLeapMinMaxes.ARM_LEFT_RIGHT
						.getAttributeMinValue());
		LeapArmMinMaxes.ARM_LEFT_RIGHT
				.setStaticMaxValue(StaticLeapMinMaxes.ARM_LEFT_RIGHT
						.getAttributeMaxValue());

		LeapArmMinMaxes.ARM_FRONT_BACK
				.setStaticMinValue(StaticLeapMinMaxes.ARM_FRONT_BACK
						.getAttributeMinValue());
		LeapArmMinMaxes.ARM_FRONT_BACK
				.setStaticMaxValue(StaticLeapMinMaxes.ARM_FRONT_BACK
						.getAttributeMaxValue());

		LeapArmMinMaxes.ARM_UP_DOWN
				.setStaticMinValue(StaticLeapMinMaxes.ARM_UP_DOWN
						.getAttributeMinValue());
		LeapArmMinMaxes.ARM_UP_DOWN
				.setStaticMaxValue(StaticLeapMinMaxes.ARM_UP_DOWN
						.getAttributeMaxValue());

		LeapArmMinMaxes.PITCH.setStaticMinValue(StaticLeapMinMaxes.PITCH
				.getAttributeMinValue());
		LeapArmMinMaxes.PITCH.setStaticMaxValue(StaticLeapMinMaxes.PITCH
				.getAttributeMaxValue());

		LeapArmMinMaxes.ROLL.setStaticMinValue(StaticLeapMinMaxes.ROLL
				.getAttributeMinValue());
		LeapArmMinMaxes.ROLL.setStaticMaxValue(StaticLeapMinMaxes.ROLL
				.getAttributeMaxValue());

		LeapArmMinMaxes.FINGERS_DISTANCE
				.setStaticMinValue(StaticLeapMinMaxes.FINGERS_DISTANCE
						.getAttributeMinValue());
		LeapArmMinMaxes.FINGERS_DISTANCE
				.setStaticMaxValue(StaticLeapMinMaxes.FINGERS_DISTANCE
						.getAttributeMaxValue());

	}

	public void setLipMinMaxesHardcoredValues() {
		LeapArmMinMaxes.ARM_LEFT_RIGHT
				.setStaticMinValue(StaticLeapMinMaxes.ARM_LEFT_RIGHT
						.getHardcoredMinValue());
		LeapArmMinMaxes.ARM_LEFT_RIGHT
				.setStaticMaxValue(StaticLeapMinMaxes.ARM_LEFT_RIGHT
						.getHardcoredMaxValue());

		LeapArmMinMaxes.ARM_FRONT_BACK
				.setStaticMinValue(StaticLeapMinMaxes.ARM_FRONT_BACK
						.getHardcoredMinValue());
		LeapArmMinMaxes.ARM_FRONT_BACK
				.setStaticMaxValue(StaticLeapMinMaxes.ARM_FRONT_BACK
						.getHardcoredMaxValue());

		LeapArmMinMaxes.ARM_UP_DOWN
				.setStaticMinValue(StaticLeapMinMaxes.ARM_UP_DOWN
						.getHardcoredMinValue());
		LeapArmMinMaxes.ARM_UP_DOWN
				.setStaticMaxValue(StaticLeapMinMaxes.ARM_UP_DOWN
						.getHardcoredMaxValue());

		LeapArmMinMaxes.PITCH.setStaticMinValue(StaticLeapMinMaxes.PITCH
				.getHardcoredMinValue());
		LeapArmMinMaxes.PITCH.setStaticMaxValue(StaticLeapMinMaxes.PITCH
				.getHardcoredMaxValue());

		LeapArmMinMaxes.ROLL.setStaticMinValue(StaticLeapMinMaxes.ROLL
				.getHardcoredMinValue());
		LeapArmMinMaxes.ROLL.setStaticMaxValue(StaticLeapMinMaxes.ROLL
				.getHardcoredMaxValue());

		LeapArmMinMaxes.FINGERS_DISTANCE
				.setStaticMinValue(StaticLeapMinMaxes.FINGERS_DISTANCE
						.getHardcoredMinValue());
		LeapArmMinMaxes.FINGERS_DISTANCE
				.setStaticMaxValue(StaticLeapMinMaxes.FINGERS_DISTANCE
						.getHardcoredMaxValue());

	}

	public void setLeapDistanceOfFingersBoundary(float distanceOfFingers) {

		System.out.println("Finger distance min : "
				+ LeapArmMinMaxes.FINGERS_DISTANCE.getAttributeMinValue()
				+ "Finger distance max : "
				+ LeapArmMinMaxes.FINGERS_DISTANCE.getAttributeMaxValue());

		if (distanceOfFingers > LeapArmMinMaxes.FINGERS_DISTANCE
				.getStaticMaxValue()) {
			LeapArmMinMaxes.FINGERS_DISTANCE
					.setAttributeMaxValue(distanceOfFingers);
			LeapArmMinMaxes.FINGERS_DISTANCE.setStaticMaxValue(distanceOfFingers);
		}
		if (distanceOfFingers < LeapArmMinMaxes.FINGERS_DISTANCE
				.getStaticMinValue()) {
			LeapArmMinMaxes.FINGERS_DISTANCE
					.setAttributeMinValue(distanceOfFingers);
			LeapArmMinMaxes.FINGERS_DISTANCE.setStaticMinValue(distanceOfFingers);
		}

	}

	public void setLeapRollBoundary(float handRoll) {

		System.out.println("Arm roll min : "
				+ LeapArmMinMaxes.ROLL.getAttributeMinValue() + "Arm roll max : "
				+ LeapArmMinMaxes.ROLL.getAttributeMaxValue());
		if (handRoll > LeapArmMinMaxes.ROLL.getStaticMaxValue()) {
			LeapArmMinMaxes.ROLL.setAttributeMaxValue(handRoll);
			LeapArmMinMaxes.ROLL.setStaticMaxValue(handRoll);
		}
		if (handRoll < LeapArmMinMaxes.ROLL.getStaticMinValue()) {
			LeapArmMinMaxes.ROLL.setAttributeMinValue(handRoll);
			LeapArmMinMaxes.ROLL.setStaticMinValue(handRoll);
		}

	}

	public void setLeapPitchBoundary(float handPitch) {
		System.out.println("Arm pitch min : "
				+ LeapArmMinMaxes.PITCH.getAttributeMinValue()
				+ "Arm pitch max : "
				+ LeapArmMinMaxes.PITCH.getAttributeMaxValue());
		if (handPitch > LeapArmMinMaxes.PITCH.getStaticMaxValue()) {
			LeapArmMinMaxes.PITCH.setAttributeMaxValue(handPitch);
			LeapArmMinMaxes.PITCH.setStaticMaxValue(handPitch);
		}
		if (handPitch < LeapArmMinMaxes.PITCH.getStaticMinValue()) {
			LeapArmMinMaxes.PITCH.setAttributeMinValue(handPitch);
			LeapArmMinMaxes.PITCH.setStaticMinValue(handPitch);
		}

	}

	public void setLeapUpDownBoundary(float armUpDown) {
		System.out.println("Arm down : "
				+ LeapArmMinMaxes.ARM_UP_DOWN.getAttributeMinValue() + "Arm up : "
				+ LeapArmMinMaxes.ARM_UP_DOWN.getAttributeMaxValue());
		if (armUpDown > LeapArmMinMaxes.ARM_UP_DOWN.getStaticMaxValue()) {
			LeapArmMinMaxes.ARM_UP_DOWN.setAttributeMaxValue(armUpDown);
			LeapArmMinMaxes.ARM_UP_DOWN.setStaticMaxValue(armUpDown);
		}
		if (armUpDown < LeapArmMinMaxes.ARM_UP_DOWN.getStaticMinValue()) {
			LeapArmMinMaxes.ARM_UP_DOWN.setAttributeMinValue(armUpDown);
			LeapArmMinMaxes.ARM_UP_DOWN.setStaticMinValue(armUpDown);
		}

	}

	public void setLeapLeftRightBoundary(float armLeftRight) {
		System.out.println("Arm left : "
				+ LeapArmMinMaxes.ARM_LEFT_RIGHT.getAttributeMinValue()
				+ "Arm right : "
				+ LeapArmMinMaxes.ARM_LEFT_RIGHT.getAttributeMaxValue());
		if (armLeftRight > LeapArmMinMaxes.ARM_LEFT_RIGHT.getStaticMaxValue()) {
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setAttributeMaxValue(armLeftRight);
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setStaticMaxValue(armLeftRight);
		}
		if (armLeftRight < LeapArmMinMaxes.ARM_LEFT_RIGHT.getStaticMinValue()) {
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setAttributeMinValue(armLeftRight);
			LeapArmMinMaxes.ARM_LEFT_RIGHT.setStaticMinValue(armLeftRight);
		}

	}

	public void setLeapForwardBackwardBoundary(float armForwardBackward) {
		System.out.println("Arm back : "
				+ LeapArmMinMaxes.ARM_FRONT_BACK.getAttributeMinValue()
				+ "Arm front : "
				+ LeapArmMinMaxes.ARM_FRONT_BACK.getAttributeMaxValue());
		if (armForwardBackward > LeapArmMinMaxes.ARM_FRONT_BACK
				.getStaticMaxValue()) {
			LeapArmMinMaxes.ARM_FRONT_BACK
					.setAttributeMaxValue(armForwardBackward);
			LeapArmMinMaxes.ARM_FRONT_BACK.setStaticMaxValue(armForwardBackward);
		}
		if (armForwardBackward < LeapArmMinMaxes.ARM_FRONT_BACK
				.getStaticMinValue()) {
			LeapArmMinMaxes.ARM_FRONT_BACK
					.setAttributeMinValue(armForwardBackward);
			LeapArmMinMaxes.ARM_FRONT_BACK.setStaticMinValue(armForwardBackward);
		}

	}
}