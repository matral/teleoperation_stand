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
			last_handYawRescaled,last_handLeftRight;

	private double epsilon;

	public void onConnect(Controller controllerP) {
		System.out.println("Connected");
		last_handRollRescaled = HandInitialValues.ROLL.getValue();
		last_handPitchRescaled = HandInitialValues.PITCH.getValue();
		last_handYawRescaled = HandInitialValues.YAW.getValue();
		last_handLeftRight = HandInitialValues.LEFT_RIGHT.getValue();

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

	public int[] leapHandPositionsRescaled(double handPitch,double handLeftRight, double handRoll,
			double handYaw, ConnectionBuilder connection) {
		handPitch = -handPitch;

		
		int handPitchRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handPitch,
						LeapCarMinMaxes.PITCH.getAttributeMinValue(),
						LeapCarMinMaxes.PITCH.getAttributeMaxValue(),
						RoboclawPercentageBoundaries.FORWARD_BACKWARD
								.getServoMinValue(),
						RoboclawPercentageBoundaries.FORWARD_BACKWARD
								.getServoMaxValue(), last_handPitchRescaled,
						RoboclawPercentageBoundaries.FORWARD_BACKWARD
								.getServoEpsilon(), true);
		
		
		int handRollRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handRoll, LeapCarMinMaxes.ROLL
						.getAttributeMinValue(), LeapCarMinMaxes.ROLL
						.getAttributeMaxValue(),
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoMinValue(),
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoMaxValue(), last_handRollRescaled,
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoEpsilon(), true);

	
		
		int handLeftRightRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handLeftRight, LeapCarMinMaxes.ARM_LEFT_RIGHT
						.getAttributeMinValue(), LeapCarMinMaxes.ARM_LEFT_RIGHT
						.getAttributeMaxValue(),
						RoboclawPercentageBoundaries.LEFT_RIGHT
								.getServoMinValue(),
						RoboclawPercentageBoundaries.LEFT_RIGHT
								.getServoMaxValue(), last_handLeftRight,
						RoboclawPercentageBoundaries.LEFT_RIGHT
								.getServoEpsilon(), true);
		
		
		int handYawRescaled = ServosPositionsCalulations
				.rescaleAndCheckEpsilonEcxeeding(handYaw, LeapCarMinMaxes.ROLL
						.getAttributeMinValue(), LeapCarMinMaxes.ROLL
						.getAttributeMaxValue(),
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoMinValue(),
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoMaxValue(), last_handYawRescaled,
						RoboclawPercentageBoundaries.YAW_LEFT_RIGHT
								.getServoEpsilon());

		if (last_handPitchRescaled != handPitchRescaled) {
			if (handPitchRescaled > 54 || handPitchRescaled < 46) {
				last_handPitchRescaled = handPitchRescaled;
			} else {
				last_handPitchRescaled = 50;
			}
		}
		
		if (last_handRollRescaled != handRollRescaled) {
			if (handRollRescaled > 54 || handRollRescaled < 46) {
				last_handRollRescaled = handRollRescaled;
			} else {
				last_handRollRescaled = 50;
			}
		}
		if (last_handLeftRight != handLeftRightRescaled) {
			if (handLeftRightRescaled > 54 || handLeftRightRescaled < 46) {
				last_handLeftRight = handLeftRightRescaled;
			} else {
				last_handLeftRight = 50;
			}
		}
		
		if (last_handYawRescaled != handYawRescaled) {
			if (handYawRescaled > 57 || handYawRescaled < 43) {
				last_handYawRescaled = handYawRescaled;
			} else {
				last_handYawRescaled = 50;
			}
		}
		
		System.out.println("hand roll rescaled internal: " + last_handRollRescaled);
		
		if(last_handPitchRescaled >40 && last_handPitchRescaled < 60){
		return (new int[] { last_handPitchRescaled, last_handLeftRight,
				last_handYawRescaled });
		}else{
			System.out.println("second option");
			return (new int[] { last_handPitchRescaled, last_handLeftRight,
					last_handRollRescaled });
		}
	}

}