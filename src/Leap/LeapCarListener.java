package Leap;

import Maestro.PololuConnector;
import Maestro.ServosPositionsCalulations;
import Maestro.Servo;
import Maestro.ServoBoundaries;
import Maestro.ServosInitialValues;
import Roboclaw.HandInitialValues;
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

public class LeapCarListener extends Listener {

	private int last_handPitchRescaled, last_handRollRescaled,
			last_handYawRescaled;

	private double epsilon;

	public void onConnect(Controller controllerP) {
		System.out.println("Connected");
		last_handRollRescaled = HandInitialValues.ROLL.getValue();
		last_handPitchRescaled = HandInitialValues.PITCH.getValue();
		last_handYawRescaled = HandInitialValues.YAW.getValue();

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

	public int[] leapHandPositionsRescaled(double handPitch, double handRoll,
			double handYaw, ConnectionBuilder connection) {
		
		int handPitchRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handPitch,
						LeapCarMinMaxes.PITCH.getAttributeMinValue(),
						LeapCarMinMaxes.PITCH.getAttributeMaxValue(),
						RoboclawPercentageBoundaries.FORWARD_BACKWARD
								.getServoMinValue(),
						RoboclawPercentageBoundaries.FORWARD_BACKWARD
								.getServoMaxValue(), last_handPitchRescaled,
						RoboclawPercentageBoundaries.FORWARD_BACKWARD
								.getServoEpsilon());

		int handRollRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handRoll, LeapCarMinMaxes.ROLL
						.getAttributeMinValue(), LeapCarMinMaxes.ROLL
						.getAttributeMaxValue(),
						RoboclawPercentageBoundaries.LEFT_RIGHT
								.getServoMinValue(),
						RoboclawPercentageBoundaries.LEFT_RIGHT
								.getServoMaxValue(), last_handRollRescaled,
						RoboclawPercentageBoundaries.LEFT_RIGHT
								.getServoEpsilon());

		int handYawRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handYaw, LeapCarMinMaxes.YAW
						.getAttributeMinValue(), LeapCarMinMaxes.YAW
						.getAttributeMaxValue(),
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoMinValue(),
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoMaxValue(), last_handYawRescaled,
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoEpsilon());

		if (last_handPitchRescaled != handPitchRescaled) {
			last_handPitchRescaled = handPitchRescaled;
		}
		if (last_handRollRescaled != handRollRescaled) {
			last_handRollRescaled = handRollRescaled;
		}
		if (last_handYawRescaled != handYawRescaled) {
			last_handYawRescaled = handYawRescaled;
		}

		return (new int[]{last_handPitchRescaled,last_handRollRescaled,last_handYawRescaled});
	}

}