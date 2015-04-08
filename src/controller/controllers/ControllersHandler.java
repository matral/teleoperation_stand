package controller.controllers;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.hitec.HitecProxy;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;
import Leap.LeapCarListener;
import Leap.LeapCarMinMaxes;
import Leap.LeapListener;
import Maestro.PololuConnector;
import Maestro.Servo;
import Maestro.ServosInitialValues;
import Maestro.ServosPositionsCalulations;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Vector;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.example.DataCollector;

import controller.JFrameWindow;
import controller.util.ActualArmControllerDevice;
import controller.util.ConnectionBuilder;
import controller.util.ControllerSettings;
import controller.util.RoboclawsSpeedValues;
import controller.util.ServosPositions;
import controller.util.Util;

public class ControllersHandler {

	final JFrameWindow window;
	private Controller keyboardArmController = null;
	private Controller keyboardCarController = null;
	private Controller joystickCarController = null;
	private Controller joystickArmController = null;
	private Controller keyboardJoystickController = null;
	com.leapmotion.leap.Controller leapArmController = null;
	private LeapListener leapArmListener;
	com.leapmotion.leap.Controller leapCarController = null;
	private LeapCarListener leapCarListener;
	private boolean isDebugEnabled = false;
	private Hub myoController = null;
	private DeviceListener myoListener;
	private Vector zeroVector = null;

	private ConnectionBuilder connection;

	private boolean isLeapListenerConnected = false;
	private ActualArmControllerDevice actualArmControllerDevice = ActualArmControllerDevice.KEYBOARD;
	private ArrayList<Controller> carControllersList;
	private int[] directionOfEachServo = { 0, 0, 0, 0, 0, 0 };

	private int sleep;

	private ServosPositions servosPositions;

	public ControllersHandler() {

		window = new JFrameWindow(this);

		sleep = 5;
		carControllersList = new ArrayList<>();
		searchForCarControllers();

		searchForArmControllers();

		startShowingControllerData();
	}

	private void activateKeyboardForCar() {
		if (leapCarController != null) {
			if (leapCarListener != null) {
				leapCarController.removeListener(leapCarListener);
			}
		}
		sleep = 5;
		// int keyboardSpeed = 13;
		// setSpeed(keyboardSpeed);
		Controller[] controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];

			if (controller.getType() == Controller.Type.KEYBOARD) {
				// Add new controller to the list of all controllers.
				if (keyboardCarController == null) {
					keyboardCarController = controller;

					// Add new controller to the list on the window.
				}
			}

		}
	}

	private void activateJoystickForArm() {
		if (leapArmController != null) {
			if (leapArmListener != null) {
				leapArmController.removeListener(leapArmListener);
			}
		}
		sleep = 5;
		// int keyboardSpeed = 13;
		// setSpeed(keyboardSpeed);
		Controller[] controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];
			if (controller.getType() == Controller.Type.STICK) {
				// Add new controller to the list of all controllers.
				joystickArmController = controller;
				// Add new controller to the list on the window.
			}
		}

	}

	private void createArmSkeleton() {

	}

	private void activateJoystickForCar() {
		if (leapCarController != null) {
			if (leapCarListener != null) {
				leapCarController.removeListener(leapCarListener);
			}
		}
		sleep = 5;
		// int keyboardSpeed = 13;
		// setSpeed(keyboardSpeed);
		Controller[] controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];
			if (controller.getType() == Controller.Type.STICK) {
				// Add new controller to the list of all controllers.
				joystickCarController = controller;

				// Add new controller to the list on the window.
			}
		}

	}

	private void activateKeyboardForArm() {
		if (leapArmController != null) {
			if (leapArmListener != null) {
				leapArmController.removeListener(leapArmListener);
			}
		}
		sleep = 5;
		int keyboardSpeed = 13;
		setSpeed(keyboardSpeed);
		
		Controller[] controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];

			if (controller.getType() == Controller.Type.KEYBOARD) {
				// Add new controller to the list of all controllers.
				if (keyboardArmController == null) {
					keyboardArmController = controller;

					// Add new controller to the list on the window.
				}
			}

		}
	}

	private void registerKeyboardForArm() {
		window.addArmControllerName("keyboard");
	}

	private void registerLeapForArm() {
		window.addArmControllerName("leap");
	}

	private void registerJoystickForArm() {
		window.addArmControllerName("joystick");
	}

	private void registerJoystick2DForArm() {
		window.addArmControllerName("joystick2D");
	}

	private void activateLeapForCar() {

		leapCarListener = new LeapCarListener();
		if (leapCarController == null) {
			leapCarController = new com.leapmotion.leap.Controller();
		}

	}

	private void activateLeapForArm() {
		sleep = 40;
		// int leapSpeed = 30;
		// setSpeed(leapSpeed);
		leapArmListener = new LeapListener();
		if (leapArmController == null) {
			leapArmController = new com.leapmotion.leap.Controller();
		}
		;
		leapArmListener.setLipMinMaxesHardcoredValues();
		window.setArmLeapCheckBox(getConnection());
		// leapController.addListener(leapListener);
	}

	private ConnectionBuilder getConnection() {
		return connection;
	}

	private void activateMyoForArm() {

		if (leapArmController != null) {
			if (leapArmListener != null) {
				leapArmController.removeListener(leapArmListener);
			}
		}
		myoController = new Hub("com.example.hello-myo");
		// myoController = new Hub("com.example.emg-data-sample");

		System.out.println("Attempting to find a Myo...");

		Myo myo = myoController.waitForMyo(10000);
		// myo.setStreamEmg(StreamEmgType.STREAM_EMG_ENABLED);
		myoListener = new DataCollector();
		if (myo != null) {
			window.addArmControllerName("myo");
			myoController.addListener(myoListener);
		}

	}

	private void activateJoystick2DForArm() {
		if (leapArmController != null) {
			if (leapArmListener != null) {
				leapArmController.removeListener(leapArmListener);
			}
		}
		sleep = 5;
		// int keyboardSpeed = 13;
		// setSpeed(keyboardSpeed);
		Controller[] controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];
			if (controller.getType() == Controller.Type.STICK) {
				// Add new controller to the list of all controllers.
				joystickArmController = controller;
				createArmSkeleton();
				// Add new controller to the list on the window.
			}
		}

	}

	private void searchForArmControllers() {
		// System.out.println("1");
		registerKeyboardForArm();

		registerLeapForArm();
		registerJoystickForArm();
		registerJoystick2DForArm();
	}

	private void searchForCarControllers() {
		registerKeyboardForCar();
		registerJoystickForCar();

		registerLeapForCar();

	}

	private void registerJoystickForCar() {
		window.addCarControllerName("joystick");
	}

	private void registerKeyboardForCar() {
		window.addCarControllerName("keyboard");
		/*
		 * Controller[] controllers = ControllerEnvironment
		 * .getDefaultEnvironment().getControllers(); for (int i = 0; i <
		 * controllers.length; i++) { Controller controller = controllers[i];
		 * 
		 * if (controller.getType() == Controller.Type.STICK ||
		 * controller.getType() == Controller.Type.GAMEPAD ||
		 * controller.getType() == Controller.Type.WHEEL || controller.getType()
		 * == Controller.Type.FINGERSTICK || controller.getType() ==
		 * Controller.Type.KEYBOARD /* || controller.getType() ==
		 * Controller.Type.MOUSE
		 *//*
			 * ) { // Add new controller to the list of all controllers.
			 * carControllersList.add(controller);
			 * 
			 * // Add new controller to the list on the window.
			 * window.addCarControllerName(controller.getName() + " - " +
			 * controller.getType().toString() + " type"); }
			 * 
			 * }
			 */
	}

	private void registerLeapForCar() {
		window.addCarControllerName("leap");
	}

	/**
	 * Starts showing controller data on the window.
	 */

	private ConnectionBuilder connectToPanda(String pandaIP, int pandaPort) {
		AmberClient client;
		try {
			client = new AmberClient(pandaIP, pandaPort);
		} catch (IOException e) {
			System.out.println("Unable to connect to robot: " + e);
			return null;
		}
		return new ConnectionBuilder(client, new RoboclawProxy(client, 0),
				new HitecProxy(client, 0), new com.leapmotion.leap.Controller());

	}

	private void initializeArm() {

		getConnection().getHitecProxy().setSpeed(
				ServosInitialValues.BOTTOM.getSpeed(),
				Servo.BOTTOM.getServoPort());

		getConnection().getHitecProxy().setAngle(
				ServosInitialValues.BOTTOM.getInitialPosition(),
				Servo.BOTTOM.getServoPort());

		getConnection().getHitecProxy().setSpeed(
				ServosInitialValues.DOF_1.getSpeed(),
				Servo.DOF_1.getServoPort());
		getConnection().getHitecProxy().setAngle(
				ServosInitialValues.DOF_1.getInitialPosition(),
				Servo.DOF_1.getServoPort());

		getConnection().getHitecProxy().setSpeed(
				ServosInitialValues.DOF_2.getSpeed(),
				Servo.DOF_2.getServoPort());
		getConnection().getHitecProxy().setAngle(
				ServosInitialValues.DOF_2.getInitialPosition(),
				Servo.DOF_2.getServoPort());

		getConnection().getHitecProxy().setSpeed(
				ServosInitialValues.DOF_3.getSpeed(),
				Servo.DOF_3.getServoPort());
		getConnection().getHitecProxy().setAngle(
				ServosInitialValues.DOF_3.getInitialPosition(),
				Servo.DOF_3.getServoPort());

		getConnection().getHitecProxy().setSpeed(
				ServosInitialValues.CATCHER_ROTATOR.getSpeed(),
				Servo.CATCHER_ROTATOR.getServoPort());
		getConnection().getHitecProxy().setAngle(
				ServosInitialValues.CATCHER_ROTATOR.getInitialPosition(),
				Servo.CATCHER_ROTATOR.getServoPort());

		getConnection().getHitecProxy().setSpeed(
				ServosInitialValues.CATCHER.getSpeed(),
				Servo.CATCHER.getServoPort());
		getConnection().getHitecProxy().setAngle(
				ServosInitialValues.CATCHER.getInitialPosition(),
				Servo.CATCHER.getServoPort());
		int i = 0;
		for (ServosInitialValues servo : ServosInitialValues.values()) {
			directionOfEachServo[i++] = servo.getInitialPosition();
		}
		servosPositions = new ServosPositions(
				ServosInitialValues.BOTTOM.getInitialPosition(),
				ServosInitialValues.DOF_1.getInitialPosition(),
				ServosInitialValues.DOF_2.getInitialPosition(),
				ServosInitialValues.DOF_3.getInitialPosition(),
				ServosInitialValues.CATCHER_ROTATOR.getInitialPosition(),
				ServosInitialValues.CATCHER.getInitialPosition());
	}

	private void setSpeed(int speed) {
		for (Servo servo : Servo.values()) {
			getConnection().getHitecProxy().setSpeed(speed,
					servo.getServoPort());
		}

	}

	public void setArmListeners() {
		String controllerName = window.getSelectedArmDevicesName();

		switch (controllerName) {
		case "keyboard":
			activateKeyboardForArm();
			break;
		case "leap":
			activateLeapForArm();
			break;
		case "joystick":
			activateJoystickForArm();

			break;
		case "joystick2D":
			activateJoystick2DForArm();
			break;
		}
	}

	public void setCarListeners() {
		String controllerName = window.getSelectedCarDevicesName();

		switch (controllerName) {
		case "keyboard":
			activateKeyboardForCar();
			break;
		case "leap":
			activateLeapForCar();
			break;
		case "joystick":
			activateJoystickForCar();
			break;
		case "phantom":
			// activatePhantomForArm();
			break;
		}
	}

	private void updateArm(MecanumDriver mecanumDriver) {
		String controllerName = window.getSelectedArmDevicesName();
		switch (controllerName) {
		case "keyboard":
			updateArmKeyboard();
			break;
		case "leap":
			updateArmLeap();
			break;

		case "joystick":
			updateArmJoystick();
			break;

		case "joystick2D":
			updateArmJoystick2D();
			break;
		}

	}

	private void updateArmMyo() {
		myoController.runOnce(10);
	}

	private void updateArmPhantom() {

	}

	private void updateArmLeap() {
		if (leapArmController != null) {

			Frame frame = leapArmController.frame();
			if (!frame.hands().isEmpty()) {
				// Get the first hand
				Hand hand = frame.hands().rightmost();

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
				if (isDebugEnabled) {
					System.out.println("Hand sphere radius: "
							+ hand.sphereCenter());
					System.out.println("left-right: "
							+ hand.palmPosition().getX() + "up-down: "
							+ hand.palmPosition().getY() + "forward-backward: "
							+ -hand.palmPosition().getZ());
				}

				// Get the hand's normal vector and direction
				Vector normal = hand.palmNormal();
				Vector direction = hand.direction();

				// Calculate the hand's pitch, roll, and yaw angles
				if (isDebugEnabled) {
					System.out.println("Hand pitch: " + direction.pitch()
							+ " degrees, " + "roll: " + normal.roll()
							+ " degrees, " + "yaw: " + direction.yaw()
							+ " degrees\n");
					System.out.println("finger distance : "
							+ fingers.get(2).tipPosition()
									.distanceTo(fingers.get(3).tipPosition()));
				}
				if (!fingers.isEmpty()) {

					Vector avgPos = Vector.zero();
					int fingersCount = 0;
					for (int i = 1; i < 3; i++) {
						avgPos = avgPos.plus(fingers.get(i).direction());
						fingersCount += 1;
					}

					avgPos = avgPos.divide(fingersCount);
					if (isDebugEnabled) {
						System.out.println(avgPos);
					}
					if (leapArmController.frame().hands().count() == 2
							&& (leapArmController.frame().hands().leftmost()
									.grabStrength() == 1 || leapArmController
									.frame().hands().rightmost().grabStrength() == 1)
							&& !window.areLeapBoundariesHardcoded()
							&& window.isAtLeastOneBoundaryCheckBoxSelected()) {
						window.setArmStateInfo("Arm is not running | Select which DOF boundarie is going to be changed");
						if (window.isArmForwardBackwardCheckboxSelected()) {
							leapArmListener
									.setLeapForwardBackwardBoundary(-hand
											.palmPosition().getZ());
						}
						if (window.isArmLeftRightCheckboxSelected()) {
							leapArmListener.setLeapLeftRightBoundary(hand
									.palmPosition().getX());
						}
						if (window.isArmUpDownCheckboxSelected()) {
							leapArmListener.setLeapUpDownBoundary(hand
									.palmPosition().getY());
						}
						if (window.isHandPitchCheckboxSelected()) {
							leapArmListener.setLeapPitchBoundary(direction
									.pitch());
						}
						if (window.isHandRollCheckboxSelected()) {
							leapArmListener.setLeapRollBoundary(-normal.roll());
						}
						if (window.isDistanceOfFingersCheckboxSelected()) {
							leapArmListener
									.setLeapDistanceOfFingersBoundary(fingers
											.get(2)
											.tipPosition()
											.distanceTo(
													fingers.get(3)
															.tipPosition()));
						}
						/*
						 * leapListener.setLipMinMaxes(
						 * hand.palmPosition().getX(),
						 * hand.palmPosition().getY(),
						 * -hand.palmPosition().getZ(), direction.pitch(),
						 * -normal.roll(), avgPos.getY(), fingers.get(2)
						 * .tipPosition() .distanceTo(
						 * fingers.get(3).tipPosition()));
						 */
					} else {
						leapArmListener.setLipMinMaxesStaticValues();

						if (!window.isAtLeastOneBoundaryCheckBoxSelected()) {
							window.setArmStateInfo("Arm is running with customized boundaries");
							leapArmListener.setServos(
									hand.palmPosition().getX(),
									hand.palmPosition().getY(),
									-hand.palmPosition().getZ(),
									direction.pitch(),
									normal.roll(),
									-avgPos.getY(),
									fingers.get(2)
											.tipPosition()
											.distanceTo(
													fingers.get(3)
															.tipPosition()),
									getConnection());
						} else {
							window.setArmStateInfo("Arm is not running | Select which DOF boundarie is going to be changed");
						}
					}

				}
			}
		}
	}

	private void updateCarLeap(MecanumDriver mecanumDriver) {
		if (leapCarController != null) {

			Frame frame = leapCarController.frame();
			if (!frame.hands().isEmpty()) {
				// Get the first hand
				Hand hand = frame.hands().rightmost();

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

				if (isDebugEnabled) {
					System.out.println("Hand sphere radius: "
							+ hand.sphereCenter());
					System.out.println("left-right: "
							+ hand.palmPosition().getX() + "up-down: "
							+ hand.palmPosition().getY() + "forward-backward: "
							+ -hand.palmPosition().getZ());
				}

				// Get the hand's normal vector and direction
				Vector normal = hand.palmNormal();
				Vector direction = hand.direction();

				// Calculate the hand's pitch, roll, and yaw angles
				if (isDebugEnabled) {
					System.out.println("Hand pitch: " + direction.pitch()
							+ " degrees, " + "roll: " + normal.roll()
							+ " degrees, " + "yaw: " + direction.yaw()
							+ " degrees\n");
					System.out.println("finger distance : "
							+ fingers.get(2).tipPosition()
									.distanceTo(fingers.get(3).tipPosition()));
				}
				if (!fingers.isEmpty()) {

					Vector avgPos = Vector.zero();
					int fingersCount = 0;
					for (int i = 1; i < 3; i++) {
						avgPos = avgPos.plus(fingers.get(i).direction());
						fingersCount += 1;
					}

					avgPos = avgPos.divide(fingersCount);
					if (isDebugEnabled) {
						System.out.println(avgPos);
					}
					if (leapCarController.frame().hands().leftmost()
							.grabStrength() == 1) {
						if (isDebugEnabled) {
							System.out.println("pitch: " + direction.pitch());
							System.out.println("yaw: " + direction.yaw());
							System.out.println("roll: " + normal.roll());
						}
						LeapCarMinMaxes.ARM_LEFT_RIGHT
								.setAttributeMinMaxValue(-hand.palmPosition()
										.getX());
						moveRoboClaws(mecanumDriver, 50, 50, 50, connection);

					} else {
						int pitchPercentage;
						int yawPercentage;
						int leftRightPercentage;
						int[] calculatedLeapHandPercentage;
						/*
						 * calculatedLeapHandPercentage = leapCarListener
						 * .leapHandPositionsRescaled(direction.pitch(),
						 * normal.roll(), direction.yaw(), connection);
						 */

						calculatedLeapHandPercentage = leapCarListener
								.leapHandPositionsRescaled(direction.pitch(),
										-hand.palmPosition().getX(),
										normal.roll(), direction.yaw(),
										connection);

						pitchPercentage = calculatedLeapHandPercentage[0];
						leftRightPercentage = calculatedLeapHandPercentage[1];
						yawPercentage = calculatedLeapHandPercentage[2];

						if (isDebugEnabled) {
							System.out.println("pitch: " + pitchPercentage);
							System.out.println("yaw: " + yawPercentage);
							System.out.println("roll: " + leftRightPercentage);
						}

						moveRoboClaws(mecanumDriver, leftRightPercentage,
								pitchPercentage, yawPercentage, connection);
					}

				}
			}
		}
	}

	private void updateArmKeyboard() {

		int[] keys_arm = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		Controller controller_arm = keyboardArmController;

		if (!controller_arm.poll()) {
			window.showArmDevicesNameDisconnected();
			return;
		}

		JPanel buttonsPanel_arm = new JPanel(new FlowLayout(FlowLayout.LEFT, 1,
				1));
		buttonsPanel_arm.setBounds(6, 19, 250, 210);

		Component[] components_arm = controller_arm.getComponents();
		for (int i = 0; i < components_arm.length; i++) {
			Component component_arm = components_arm[i];

			Identifier componentIdentifier_arm = component_arm.getIdentifier();

			if (componentIdentifier_arm.getName().matches(
					"N||M||V||G||H||U||I||J||9||K||O||P")) {
				// Is button pressed?
				boolean isItPressed = true;
				if (component_arm.getPollData() == 0.0f)
					isItPressed = false;
				// Button index
				String buttonIndex;
				buttonIndex = component_arm.getIdentifier().toString();

				JToggleButton aToggleButton_arm = new JToggleButton(
						buttonIndex, isItPressed);
				aToggleButton_arm.setPreferredSize(new Dimension(48, 25));
				aToggleButton_arm.setEnabled(false);
				buttonsPanel_arm.add(aToggleButton_arm);

				if (isItPressed) {
					switch (componentIdentifier_arm.getName()) {
					case "N":
						keys_arm[0] = 1;
						break;
					case "M":
						keys_arm[1] = 1;
						break;
					case "V":
						keys_arm[2] = 1;
						break;
					case "G":
						keys_arm[3] = 1;
						break;
					case "U":
						keys_arm[4] = 1;
						break;
					case "H":
						keys_arm[5] = 1;
						break;
					case "I":
						keys_arm[6] = 1;
						break;
					case "9":
						keys_arm[7] = 1;
						break;
					case "K":
						keys_arm[8] = 1;
						break;
					case "J":
						keys_arm[9] = 1;
						break;
					case "O":
						keys_arm[10] = 1;
						break;
					case "P":
						keys_arm[11] = 1;
						break;

					default:
						break;
					}
				}
				continue;
			}
			window.setArmDevicesButtons(buttonsPanel_arm);

		}
		if (controller_arm.getType() == Controller.Type.KEYBOARD) {
			calculateDirectionOfEachServo(keys_arm);
			isDebugEnabled = true;
			if (keys_arm[0] + keys_arm[1] != 0) {
				if (isDebugEnabled) {
					System.out.println(servosPositions.getBottomPosition());
				}
				connection.getHitecProxy().setAngle(
						Servo.BOTTOM.getServoPort(),
						servosPositions.getBottomPosition());

				/*
				 * PololuConnector.setTarget(servosPositions.getBottomPosition(),
				 * Servo.BOTTOM.getServoPort());
				 */}
			if (keys_arm[2] + keys_arm[3] != 0) {
				if (isDebugEnabled) {
					System.out.println(servosPositions.getDOF1Position());
				}
				getConnection().getHitecProxy().setAngle(
						Servo.DOF_1.getServoPort(),
						servosPositions.getDOF1Position());
				/*
				 * PololuConnector.setTarget(servosPositions.getDOF1Position(),
				 * Servo.DOF_1.getServoPort());
				 */
			}
			if (keys_arm[4] + keys_arm[5] != 0) {
				if (isDebugEnabled) {
					System.out.println(servosPositions.getDOF2Position());
				}
				getConnection().getHitecProxy().setAngle(
						Servo.DOF_2.getServoPort(),
						servosPositions.getDOF2Position());
				/*
				 * PololuConnector.setTarget(servosPositions.getDOF2Position(),
				 * Servo.DOF_2.getServoPort());
				 */
			}

			if (keys_arm[6] + keys_arm[7] != 0) {
				if (isDebugEnabled) {
					System.out.println(servosPositions.getDOF3Position());
				}
				getConnection().getHitecProxy().setAngle(
						Servo.DOF_3.getServoPort(),
						servosPositions.getDOF3Position());
				/*
				 * PololuConnector.setTarget(servosPositions.getDOF3Position(),
				 * Servo.DOF_3.getServoPort());
				 */
			}

			if (keys_arm[8] + keys_arm[9] != 0) {
				if (isDebugEnabled) {
					System.out.println(servosPositions
							.getCatcherRotatorPosition());
				}
				getConnection().getHitecProxy().setAngle(
						Servo.CATCHER_ROTATOR.getServoPort(),
						servosPositions.getCatcherRotatorPosition());
				/*
				 * PololuConnector.setTarget(
				 * servosPositions.getCatcherRotatorPosition(),
				 * Servo.CATCHER_ROTATOR.getServoPort());
				 */
			}

			if (keys_arm[10] + keys_arm[11] != 0) {
				if (isDebugEnabled) {
					System.out.println(servosPositions.getCatcherPosition());
				}
				getConnection().getHitecProxy().setAngle(
						Servo.CATCHER.getServoPort(),
						servosPositions.getCatcherPosition());
				/*
				 * PololuConnector.setTarget(servosPositions.getCatcherPosition()
				 * , Servo.CATCHER.getServoPort());
				 */
			}
			isDebugEnabled = false;
		}
	}

	private void setAllServos() {
		PololuConnector.setTarget(servosPositions.getBottomPosition(),
				Servo.BOTTOM.getServoPort());

		PololuConnector.setTarget(servosPositions.getDOF1Position(),
				Servo.DOF_1.getServoPort());

		PololuConnector.setTarget(servosPositions.getDOF2Position(),
				Servo.DOF_2.getServoPort());

		PololuConnector.setTarget(servosPositions.getDOF3Position(),
				Servo.DOF_3.getServoPort());

		PololuConnector.setTarget(servosPositions.getCatcherRotatorPosition(),
				Servo.CATCHER_ROTATOR.getServoPort());

		PololuConnector.setTarget(servosPositions.getCatcherPosition(),
				Servo.CATCHER.getServoPort());
	}

	private void updateCarKeyboard(MecanumDriver mecanumDriver) {

		int[] keys = { 0, 0, 0, 0, 0, 0 };
		Controller controller_car = keyboardCarController;

		if (!controller_car.poll()) {
			window.showCarDevicesNameDisconnected();
			return;
		}
		// Currently selected controller.

		// X axis and Y axis
		int xAxisPercentage = 0, yAxisPercentage = 0, zAxisPercentage = 0;
		// JPanel for other axes.
		JPanel axesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 2));
		axesPanel.setBounds(0, 0, 200, 190);

		// JPanel for controller buttons
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
		buttonsPanel.setBounds(6, 19, 250, 210);

		// Go trough all components of the controller.
		Component[] components = controller_car.getComponents();

		// car controllers
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];

			Identifier componentIdentifier = component.getIdentifier();

			// Buttons
			// if(component.getName().contains("Button")){ // If the
			// language is not english, this won't work.
			if (componentIdentifier.getName().matches("^[0-9]*$")
					|| componentIdentifier.getName().matches("^[A-Z]*$")) {
				// Is button pressed?
				boolean isItPressed = true;
				if (component.getPollData() == 0.0f)
					isItPressed = false;

				// Button index
				String buttonIndex;
				buttonIndex = component.getIdentifier().toString();

				// Create and add new button to panel.
				JToggleButton aToggleButton = new JToggleButton(buttonIndex,
						isItPressed);
				aToggleButton.setPreferredSize(new Dimension(48, 25));
				aToggleButton.setEnabled(false);
				buttonsPanel.add(aToggleButton);

				if (isItPressed) {
					switch (componentIdentifier.getName()) {
					case "A":
						keys[0] = 1;
						break;
					case "D":
						keys[1] = 1;
						break;
					case "W":
						keys[2] = 1;
						break;
					case "S":
						keys[3] = 1;
						break;
					case "Q":
						keys[4] = 1;
						break;
					case "E":
						keys[5] = 1;
						break;

					default:
					}
				}

				// We know that this component was button so we can skip to
				// next component.
				continue;
			}

		}
		int[] calculatedKeyBoardAxisPercentage;
		if (controller_car.getType() == Controller.Type.KEYBOARD) {
			calculatedKeyBoardAxisPercentage = calculateKeyBoardAxisPercentage(keys);
			xAxisPercentage = calculatedKeyBoardAxisPercentage[0];
			yAxisPercentage = calculatedKeyBoardAxisPercentage[1];
			zAxisPercentage = calculatedKeyBoardAxisPercentage[2];

		}
		window.setControllerButtons(buttonsPanel);
		// set x and y axes,
		window.setXYAxis(xAxisPercentage, yAxisPercentage);
		// add other axes panel to window.
		window.addAxisPanel(axesPanel);
		moveRoboClaws(mecanumDriver, xAxisPercentage, yAxisPercentage,
				zAxisPercentage, getConnection());
	}

	private void updateArmJoystick2D() {

		int[] keys = { 0, 0, 0, 0, 0, 0 };

		// Currently selected controller.

		Controller controller = joystickArmController;

		// Pull controller for current data, and break while loop if
		// controller is disconnected.
		if (!controller.poll()) {
			window.showArmDevicesNameDisconnected();
			return;
		}

		// X axis and Y axis
		int xAxisPercentage = 0, yAxisPercentage = 0, zAxisPercentage = 0, sliderPercentage = 0;
		// JPanel for other axes.
		JPanel axesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 2));
		axesPanel.setBounds(0, 0, 200, 190);

		// JPanel for controller buttons
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
		buttonsPanel.setBounds(6, 19, 250, 210);

		// Go trough all components of the controller.
		Component[] components = controller.getComponents();

		// car controllers
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];

			Identifier componentIdentifier = component.getIdentifier();

			// Buttons
			// if(component.getName().contains("Button")){ // If the
			// language is not english, this won't work.
			if (componentIdentifier.getName().matches("^[0-9]*$")
					|| componentIdentifier.getName().matches("^[A-Z]*$")) {
				// Is button pressed?
				boolean isItPressed = true;
				if (component.getPollData() == 0.0f)
					isItPressed = false;

				// Button index
				String buttonIndex;
				buttonIndex = component.getIdentifier().toString();

				// Create and add new button to panel.
				JToggleButton aToggleButton = new JToggleButton(buttonIndex,
						isItPressed);
				aToggleButton.setPreferredSize(new Dimension(48, 25));
				aToggleButton.setEnabled(false);
				buttonsPanel.add(aToggleButton);

				if (isItPressed) {
					switch (componentIdentifier.getName()) {
					case "A":
						keys[0] = 1;
						break;
					case "D":
						keys[1] = 1;
						break;
					case "W":
						keys[2] = 1;
						break;
					case "S":
						keys[3] = 1;
						break;
					case "Q":
						keys[4] = 1;
						break;
					case "E":
						keys[5] = 1;
						break;

					default:
					}
				}

				// We know that this component was button so we can skip to
				// next component.
				continue;
			}
			// else{System.out.println(componentIdentifier.getName());}
			// Hat switch
			if (componentIdentifier == Component.Identifier.Axis.POV) {
				float hatSwitchPosition = component.getPollData();
				window.setArmHatSwitch(hatSwitchPosition);

				// We know that this component was hat switch so we can skip
				// to next component.
				continue;
			}

			// Axes
			if (component.isAnalog()) {

				int axisValueInPercentage = Util
						.getAxisValueInPercentage(component.getPollData());

				// X axis
				if (componentIdentifier == Component.Identifier.Axis.X) {
					xAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				// Y axis
				if (componentIdentifier == Component.Identifier.Axis.Y) {
					yAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				if (componentIdentifier == Component.Identifier.Axis.RZ) {
					zAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				// Other axis
				JLabel progressBarLabel = new JLabel(component.getName());
				JProgressBar progressBar = new JProgressBar(0, 100);
				sliderPercentage = axisValueInPercentage;
				progressBar.setValue(axisValueInPercentage);
				axesPanel.add(progressBarLabel);
				axesPanel.add(progressBar);
			}

		}

		/*
		 * sliderPercentage - przepustnica yAxisPercentage - przod tyl
		 * 
		 * xAxisPercentage - bottom
		 * 
		 * zAxisPercentage - catcher_rotator
		 * 
		 * przyciski - catcher
		 */

		xAxisPercentage = (int) ServosPositionsCalulations
				.rescaleValueFromOldMinMax(xAxisPercentage, 0, 100, -140, 140);

		yAxisPercentage = (int) ServosPositionsCalulations
				.rescaleValueFromOldMinMax(yAxisPercentage, 0, 100, 90, 180);

		yAxisPercentage = 180 + 90 - yAxisPercentage;

		sliderPercentage = (int) ServosPositionsCalulations
				.rescaleValueFromOldMinMax(sliderPercentage, 0, 100, 112, 180);

		System.out.println("xAxis : " + xAxisPercentage);
		System.out.println("yAxis : " + yAxisPercentage);
		System.out.println("zAxis : " + sliderPercentage);

		int adder = 80;
		setSkeletonAndMoveArm(xAxisPercentage, yAxisPercentage,
				sliderPercentage);

		window.setArmDevicesButtons(buttonsPanel);
		// set x and y axes,
		window.setArmXYAxis(xAxisPercentage, yAxisPercentage);
		// add other axes panel to window.
		window.addArmAxisPanel(axesPanel);

	}

	private void setSkeletonAndMoveArm(double x, double y, double z) {

	}

	private void updateArmJoystick() {

		int[] keys = { 0, 0 };

		// Currently selected controller.

		Controller controller = joystickArmController;

		// Pull controller for current data, and break while loop if
		// controller is disconnected.
		if (!controller.poll()) {
			window.showArmDevicesNameDisconnected();
			return;
		}

		// X axis and Y axis
		int xAxisPercentage = 0, yAxisPercentage = 0, zAxisPercentage = 0, sliderPercentage = 0;
		// JPanel for other axes.
		JPanel axesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 2));
		axesPanel.setBounds(0, 0, 200, 190);

		// JPanel for controller buttons
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
		buttonsPanel.setBounds(6, 19, 250, 210);

		// Go trough all components of the controller.
		Component[] components = controller.getComponents();

		// car controllers
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];

			Identifier componentIdentifier = component.getIdentifier();

			// Buttons
			// if(component.getName().contains("Button")){ // If the
			// language is not english, this won't work.
			if (componentIdentifier.getName().matches("^[0-9]*$")
					|| componentIdentifier.getName().matches("^[A-Z]*$")) {
				// Is button pressed?
				boolean isItPressed = true;
				if (component.getPollData() == 0.0f)
					isItPressed = false;

				// Button index
				String buttonIndex;
				buttonIndex = component.getIdentifier().toString();

				// Create and add new button to panel.
				JToggleButton aToggleButton = new JToggleButton(buttonIndex,
						isItPressed);
				aToggleButton.setPreferredSize(new Dimension(48, 25));
				aToggleButton.setEnabled(false);
				buttonsPanel.add(aToggleButton);

				if (isItPressed) {
					switch (componentIdentifier.getName()) {
					case "2":
						keys[0] = 1;
						break;
					case "3":
						keys[1] = 1;
						break;

					default:
					}
				}

				// We know that this component was button so we can skip to
				// next component.
				continue;
			}
			// else{System.out.println(componentIdentifier.getName());}
			// Hat switch
			if (componentIdentifier == Component.Identifier.Axis.POV) {
				float hatSwitchPosition = component.getPollData();
				window.setArmHatSwitch(hatSwitchPosition);

				// We know that this component was hat switch so we can skip
				// to next component.
				continue;
			}

			// Axes
			if (component.isAnalog()) {

				int axisValueInPercentage = Util
						.getAxisValueInPercentage(component.getPollData());

				// X axis
				if (componentIdentifier == Component.Identifier.Axis.X) {
					xAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				// Y axis
				if (componentIdentifier == Component.Identifier.Axis.Y) {
					yAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				if (componentIdentifier == Component.Identifier.Axis.RZ) {
					zAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				// Other axis
				JLabel progressBarLabel = new JLabel(component.getName());
				JProgressBar progressBar = new JProgressBar(0, 100);
				sliderPercentage = axisValueInPercentage;
				progressBar.setValue(axisValueInPercentage);
				axesPanel.add(progressBarLabel);
				axesPanel.add(progressBar);
			}

		}

		/*
		 * sliderPercentage - przepustnica yAxisPercentage - przod tyl
		 * 
		 * xAxisPercentage - bottom
		 * 
		 * zAxisPercentage - catcher_rotator
		 * 
		 * przyciski - catcher
		 */

		xAxisPercentage = (int) ServosPositionsCalulations
				.rescaleValueFromOldMinMax(xAxisPercentage, 0, 100, -49, 50);
		
		xAxisPercentage = 50 -49 - xAxisPercentage;
		yAxisPercentage = (int) ServosPositionsCalulations
				.rescaleValueFromOldMinMax(yAxisPercentage, 0, 100, 1, 180);
		zAxisPercentage = (int) ServosPositionsCalulations
				.rescaleValueFromOldMinMax(zAxisPercentage, 0, 100, 1, 180);

		yAxisPercentage = 180 + 1 -yAxisPercentage; 
		sliderPercentage = (int) ServosPositionsCalulations
				.rescaleValueFromOldMinMax(sliderPercentage, 0, 100, 1, 80);
		
		sliderPercentage = 80 + 1  - sliderPercentage;
		
		System.out.println("yAxis : " + xAxisPercentage);
		System.out.println("xAxis : " + yAxisPercentage);
		System.out.println("zAxis : " + sliderPercentage);

		
		
		calculateGripStrength(keys);

		if (keys[0] + keys[1] != 0) {
			if (isDebugEnabled) {
				System.out.println(servosPositions.getBottomPosition());
			}
			connection.getHitecProxy().setAngle(
					Servo.CATCHER.getServoPort(),
					servosPositions.getCatcherPosition());
		}
		
		set_arm_own(yAxisPercentage,xAxisPercentage,sliderPercentage,zAxisPercentage);
		window.setArmDevicesButtons(buttonsPanel);
		// set x and y axes,
		window.setArmXYAxis(xAxisPercentage, yAxisPercentage);
		// add other axes panel to window.
		window.addArmAxisPanel(axesPanel);

	}
	
		double a =100;
	    double b = 100;
	    double ed = 90;
	    
	    void set_arm_own(double x, double y, double z, double grip_angle_d){
	    	
	    	double temp_x = x;
	    	double l = Math.sqrt(x*x + y*y);
	    	double base_angle_r = Math.atan(y/x);
	    	x = l;
	    	z = z + ed;
	    	double c = Math.sqrt((x*x) + (z*z));
	    	double dof2_angle_r = Math.acos((a*a+b*b-c*c)/(2*a*b));
	    	double theta = Math.acos((c*c+b*b-a*a)/(2*b*c));
	    	double ang = Math.atan(z/x) + theta;
	    	double dof1_angle_r = Math.atan(z/x) + theta;
	    	double x1 = b*Math.cos(ang);
	    	double z1 = b*Math.sin(ang);
	    	double d = Math.sqrt((x-x1)*(x-x1) + (z-ed-z1)*(z-ed-z1));
	    	double dof3_angle_r = Math.acos((a*a+ed*ed-d*d)/(2*a*ed));
	    	double bottom_angle_r = Math.atan(y/temp_x);
	    	
	    	double bottom_angle_d = Math.toDegrees(bottom_angle_r);
	    	double dof1_angle_d = Math.toDegrees(dof1_angle_r);
	    	double dof2_angle_d = Math.toDegrees(dof2_angle_r);
	    	double dof3_angle_d = Math.toDegrees(dof3_angle_r);
	    	bottom_angle_d += 90;
	    	
	    	//bottom_angle_d += 90;
	    	dof2_angle_d = 180 - dof2_angle_d;
	    	//dof3_angle_d = 180 -dof3_angle_d;
	    	dof1_angle_d +=20;
			
			if (!( Double.isNaN(dof1_angle_d) ||Double.isNaN(bottom_angle_d)
					|| Double.isNaN(dof2_angle_d) || Double.isNaN(dof3_angle_d))) {
				System.out.println("bottom_angle_d: " + bottom_angle_d);
		    	System.out.println("dof1_angle_d: " + dof1_angle_d);
				System.out.println("dof2_angle_d: " + dof2_angle_d);
				System.out.println("dof3_angle_d: " + dof3_angle_d);
				
				if (bottom_angle_d > 0 && bottom_angle_d < 180) {
					connection.getHitecProxy().setAngle(Servo.BOTTOM.getServoPort(),
							(int) bottom_angle_d);
				}
				if (dof1_angle_d > 0 && dof1_angle_d < 180) {
					connection.getHitecProxy().setAngle(Servo.DOF_1.getServoPort(),
							(int) dof1_angle_d);
				}
				if (dof2_angle_d > 0 && dof2_angle_d < 180) {
					connection.getHitecProxy().setAngle(Servo.DOF_2.getServoPort(),
							(int) dof2_angle_d);
				}
				if (dof3_angle_d > 0 && dof3_angle_d < 180) {
					connection.getHitecProxy().setAngle(Servo.DOF_3.getServoPort(),
							(int) dof3_angle_d);
				}
				if (grip_angle_d > 0 && grip_angle_d < 180) {
					connection.getHitecProxy().setAngle(Servo.CATCHER_ROTATOR.getServoPort(),
							(int) grip_angle_d);
				}

			}
			
	    }

	public void arms(double a1, double a2, double a3, double a4, double l2,
			double l3, double l4) {
		int ex = 1, ey = 1, ez = 1;
		double er = Math.cos(a1) * ex + Math.sin(a1) * ey;
		double u2 = l2 * (Math.cos(a2) * er + Math.sin(a2) * ez);
		double u3 = l3 * (Math.cos(a2 + a3) * er + Math.sin(a2 + a3) * ez);
		double u4 = l4 * (Math.cos(a2 + a3 + a4) * er + Math.sin(a2 + a3 + a4) * ez);
		System.out.println("x:  " + u2 + "y: " + u3 + "z: " + u4);
	}

	
							// length 3.94"
	//double BASE_HGT = 100.00; // base hight 2.65"
	//double HUMERUS = 100.00; // shoulder-to-elbow "bone" 5.75"
	//double ULNA = 100.00; // elbow-to-wrist "bone" 7.375"
	//double GRIPPER = 100.00; // gripper (incl.heavy duty wrist rotate mechanism)
	void set_arm_test3(double x, double y, double z, double grip_angle_d) {
		double base_angle_r = Math.atan2(y, x);
		double r = Math.hypot(x, y) - GRIPPER;
		double u3 = (r * r + z * z - HUMERUS * HUMERUS - ULNA * ULNA)
				/ (2 * HUMERUS * ULNA);
		double elb_angle_r = -Math.acos(u3);
		double v = HUMERUS + ULNA * u3;
		double w = -ULNA * Math.sqrt(1 - u3 * u3);
		double shl_angle_r = Math.atan2(v * z - w * r, v * r + w * z);
		double wri_angle_r = -shl_angle_r - elb_angle_r;

		double base_angle_d = Math.toDegrees(base_angle_r);
		double shl_angle_d = Math.toDegrees(shl_angle_r);
		double elb_angle_d = Math.toDegrees(elb_angle_r);
		double wri_angle_d = Math.toDegrees(wri_angle_r);
		
		
		wri_angle_d += 75;
		elb_angle_d = - elb_angle_d - 40;
		
		//elb_angle_d = -elb_angle_d - 50;
		//wri_angle_d += 50;
		/* conversion */
		/*
		 * base_angle_d += 91; elb_angle_d += 55; wri_angle_d += 30; shl_angle_d
		 * +=54;
		 */
		
		System.out.println("bas_angle_d: " + base_angle_d);
		System.out.println("shl_angle_d: " + shl_angle_d);
		System.out.println("elb_angle_d: " + elb_angle_d);
		System.out.println("wri_angle_d: " + wri_angle_d);

		if (!(Double.isNaN(base_angle_d) || Double.isNaN(shl_angle_d)
				|| Double.isNaN(elb_angle_d) || Double.isNaN(wri_angle_d))) {
			if (base_angle_d > 0 && base_angle_d < 180) {
				connection.getHitecProxy().setAngle(
						Servo.BOTTOM.getServoPort(), (int) base_angle_d);
			}
			if (shl_angle_d > 0 && shl_angle_d < 180) {
				connection.getHitecProxy().setAngle(Servo.DOF_1.getServoPort(),
						(int) shl_angle_d);
			}
			if (elb_angle_d > 0 && elb_angle_d < 180) {
				connection.getHitecProxy().setAngle(Servo.DOF_2.getServoPort(),
						(int) elb_angle_d);
			}
			if (wri_angle_d > 0 && wri_angle_d < 180) {
				connection.getHitecProxy().setAngle(Servo.DOF_3.getServoPort(),
						(int) wri_angle_d);
			}

		}

	}
	double BASE_HGT = 50.00; // base hight 2.65"
	double HUMERUS = 100.00; // shoulder-to-elbow "bone" 5.75"
    double ULNA = 100.00; // elbow-to-wrist "bone" 7.375"
    double GRIPPER = 100.00; // gripper (incl.heavy duty wrist rotate mechanism)
    
    //double a = 100;
    //double b = 100;
    //double ed = 100;
    
   
	void set_arm_test2(double x, double y, double z, double grip_angle_d) {
		double hum_sq = HUMERUS * HUMERUS;
		double uln_sq = ULNA * ULNA;
		double grip_angle_r = Math.toRadians(grip_angle_d); // grip angle in
															// radians for use
															// in calculations
		/* Base angle and radial distance from x,y coordinates */
		double bas_angle_r = Math.atan2(x, y);
		double rdist = Math.sqrt((x * x) + (y * y));
		/* rdist is y coordinate for the arm */
		y = rdist;
		/* Grip offsets calculated based on grip angle */
		double grip_off_z = (Math.sin(grip_angle_r)) * GRIPPER;
		double grip_off_y = (Math.cos(grip_angle_r)) * GRIPPER;
		/* Wrist position */
		double wrist_z = (z - grip_off_z) - BASE_HGT;
		double wrist_y = y - grip_off_y;
		/* Shoulder to wrist distance ( AKA sw ) */
		double s_w = (wrist_z * wrist_z) + (wrist_y * wrist_y);
		double s_w_sqrt = Math.sqrt(s_w);
		/* s_w angle to ground */
		// double a1 = atan2( wrist_y, wrist_z );
		double a1 = Math.atan2(wrist_z, wrist_y);
		/* s_w angle to humerus */
		double a2 = Math.acos(((hum_sq - uln_sq) + s_w)
				/ (2 * HUMERUS * s_w_sqrt));
		/* shoulder angle */
		double shl_angle_r = a1 + a2;
		double shl_angle_d = Math.toDegrees(shl_angle_r);
		/* elbow angle */
		double elb_angle_r = Math.acos((hum_sq + uln_sq - s_w)
				/ (2 * HUMERUS * ULNA));
		double elb_angle_d = Math.toDegrees(elb_angle_r);
		double elb_angle_dn = -(180.0 - elb_angle_d);

		/* wrist angle */
		double wri_angle_d = (grip_angle_d - elb_angle_dn) - shl_angle_d;
		double bas_angle_d = Math.toDegrees(bas_angle_r);

		/* conversion */
		
		bas_angle_d = 90 - bas_angle_d;
		shl_angle_d += 10;
		elb_angle_d = 180 - elb_angle_d + 40;
		wri_angle_d = 180 - wri_angle_d + 30;
		

		System.out.println("bas_angle_d: " + bas_angle_d);
		System.out.println("shl_angle_d: " + shl_angle_d);
		System.out.println("elb_angle_d: " + elb_angle_d);
		System.out.println("wri_angle_d: " + wri_angle_d);

		if (!(Double.isNaN(bas_angle_d) || Double.isNaN(shl_angle_d)
				|| Double.isNaN(elb_angle_d) || Double.isNaN(wri_angle_d))) {
			if (bas_angle_d > 0 && bas_angle_d < 180) {
				connection.getHitecProxy().setAngle(
						Servo.BOTTOM.getServoPort(), (int) bas_angle_d);
			}
			if (shl_angle_d > 0 && shl_angle_d < 180) {
				connection.getHitecProxy().setAngle(Servo.DOF_1.getServoPort(),
						(int) shl_angle_d);
			}
			if (elb_angle_d > 0 && elb_angle_d < 180) {
				connection.getHitecProxy().setAngle(Servo.DOF_2.getServoPort(),
						(int) elb_angle_d);
			}
			if (wri_angle_d > 0 && wri_angle_d < 180) {
				connection.getHitecProxy().setAngle(Servo.DOF_3.getServoPort(),
						(int) wri_angle_d);
			}

		}
	}

	// x - lewo prawo, y - przod tyl, z gora dol
	// hand servo - catcher rotator
	// grip servo - catcher
	// wrist servo - 3dof
	// elbow servo - 2dof
	// shoulder servo - 1dof
	// base servo - bottom
	// 90 is middle
	void set_arm(double x, double y, double z, double grip_angle_d) {
		// we have a couple of (pseudo) constants which could be pre-computed.
		// Adapt to your specific robot
		// my system has a wrist rotor (6DOF arm) which sits between wrist and
		// gripper. If you don't have this
		// remove the „wr“ element
		double bs = 100.00;
		double se = 100.00;
		double ew = 50.00;
		double wg = 80.00;// zle
		double wr = 0.00;// zle
		double gr = 60.00; // look in here in case of probnlems
		// effective gripper/wrist length is wg + wr + some fraction of gr to
		// hold target tight
		double grp = wg + wr + 0.75 * gr;
		double se2 = se * se; // squared values
		double ew2 = ew * ew;
		// and a couple of local variables
		// assume positive y direction is to front. Compensate at higher level
		// if neccesaary
		double ba = Math.toDegrees(Math.atan2(x, y)); // base angle to cancel x
		double yt, zt, yw, zw; // transformed positions of target and wrist
								// after x-cancelation
		double sw2, sw; // computed length shoulder to wrist and squared
		double gar, sa, ea, wa, sw_gnd, sw_se, se_ew, ew_gr; // computed angles
																// in rad, world
																// coordinates
		// compute target position after x transform
		yt = Math.sqrt((x * x) + (y * y));
		zt = z; // z unchanged
		// compute wrist position. assume grip_angle_d = 0 is horizontal,
		// positive value is upwards hence wrist for pos angle will be BELOW
		// target
		gar = Math.toRadians(grip_angle_d); // we subtract the base height
											// later( or here and then also from
											// shoulder)
		zw = zt - grp * Math.sin(gar);
		if (0.0 > zw)
			System.out.println("imposiibility"); // shouldn't be negative
		yw = yt - grp * Math.cos(gar);
		if (0.0 > yw)
			System.out.println("impossibility");
		// compute sw, distance shoulder (at y=0) and wrist. compensate base
		// height if not subtracted already
		sw2 = (zw - bs) * (zw - bs) + yw * yw; // squared
		sw = Math.sqrt(sw2);
		// angle of sw relative to gnd
		sw_gnd = Math.atan2((zw - bs), yw);
		// angle sw to se via cosine law see drawing
		sw_se = Math.acos((sw2 + se2 - ew2) / (2 * se * sw));
		// angle se to ew via cosine law
		se_ew = Math.acos((ew2 + se2 - sw2) / (2 * se * ew));
		// shoulder angle relative to gnd: sw_gnd + sw_se. needs conversion as 0
		// angle is up in servo world
		sa = Math.toDegrees(sw_gnd + sw_se);
		// elbow angle , converted
		ea = Math.toDegrees(se_ew);
		// wrist angle: target grip angle minus elbow minis shoulder
		// initially, wrist is pointing to the direction given by shoulder and
		// ellbow
		wa = sa - (180 - ea);
		// offset to point the gripper to the target direction
		wa = grip_angle_d - wa;
		// this is the end of world coordinate computation ...
		// convert to robot =>
		// final step is to transform into robot coordinates
		// the following transformations depend on the actual robot
		// my system ....
		// all joints (except base) rotate along X-axis
		// 0° is upright (in Z) orientation
		// increasing angles rotate towards front (follow
		// "right hand rule",positive X to right)
		sa = -sa;
		// 0° arm = 90° world, 0° world = 90° arm
		ea = 180 - ea;
		// residual angle from triangle cosine lawcalculation
		wa = -219 + wa;
		// negated
		if (-0.1 < ba)
			ba = 180.0 - ba;
		else
			ba = -(180.0 + ba);
		if (!(Double.isNaN(ba) || Double.isNaN(sa) || Double.isNaN(ea) || Double
				.isNaN(wa))) {
			System.out.println("ba: " + ba);
			System.out.println("sa: " + sa);
			System.out.println("ea: " + ea);
			System.out.println("wa: " + wa);

			connection.getHitecProxy().setAngle(Servo.BOTTOM.getServoPort(),
					(int) ba);
			connection.getHitecProxy().setAngle(Servo.DOF_1.getServoPort(),
					(int) sa);
			connection.getHitecProxy().setAngle(Servo.DOF_2.getServoPort(),
					(int) ea);
			connection.getHitecProxy().setAngle(Servo.DOF_3.getServoPort(),
					(int) wa);
		} else {
			System.out.println("nan");
		}
	}

	private void updateCarJoystick(MecanumDriver mecanumDriver) {

		int[] keys = { 0, 0, 0, 0, 0, 0 };

		// Currently selected controller.

		Controller controller = joystickCarController;

		// Pull controller for current data, and break while loop if
		// controller is disconnected.
		if (!controller.poll()) {
			window.showCarDevicesNameDisconnected();
			return;
		}

		// X axis and Y axis
		int xAxisPercentage = 0, yAxisPercentage = 0, zAxisPercentage = 0;
		// JPanel for other axes.
		JPanel axesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 2));
		axesPanel.setBounds(0, 0, 200, 190);

		// JPanel for controller buttons
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
		buttonsPanel.setBounds(6, 19, 250, 210);

		// Go trough all components of the controller.
		Component[] components = controller.getComponents();

		// car controllers
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];

			Identifier componentIdentifier = component.getIdentifier();

			// Buttons
			// if(component.getName().contains("Button")){ // If the
			// language is not english, this won't work.
			if (componentIdentifier.getName().matches("^[0-9]*$")
					|| componentIdentifier.getName().matches("^[A-Z]*$")) {
				// Is button pressed?
				boolean isItPressed = true;
				if (component.getPollData() == 0.0f)
					isItPressed = false;

				// Button index
				String buttonIndex;
				buttonIndex = component.getIdentifier().toString();

				// Create and add new button to panel.
				JToggleButton aToggleButton = new JToggleButton(buttonIndex,
						isItPressed);
				aToggleButton.setPreferredSize(new Dimension(48, 25));
				aToggleButton.setEnabled(false);
				buttonsPanel.add(aToggleButton);

				if (isItPressed) {
					switch (componentIdentifier.getName()) {
					case "A":
						keys[0] = 1;
						break;
					case "D":
						keys[1] = 1;
						break;
					case "W":
						keys[2] = 1;
						break;
					case "S":
						keys[3] = 1;
						break;
					case "Q":
						keys[4] = 1;
						break;
					case "E":
						keys[5] = 1;
						break;

					default:
					}
				}

				// We know that this component was button so we can skip to
				// next component.
				continue;
			}
			// else{System.out.println(componentIdentifier.getName());}
			// Hat switch
			if (componentIdentifier == Component.Identifier.Axis.POV) {
				float hatSwitchPosition = component.getPollData();
				window.setHatSwitch(hatSwitchPosition);

				// We know that this component was hat switch so we can skip
				// to next component.
				continue;
			}

			// Axes
			if (component.isAnalog()) {

				int axisValueInPercentage = Util
						.getAxisValueInPercentage(component.getPollData());

				// X axis
				if (componentIdentifier == Component.Identifier.Axis.X) {
					xAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				// Y axis
				if (componentIdentifier == Component.Identifier.Axis.Y) {
					yAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				if (componentIdentifier == Component.Identifier.Axis.RZ) {
					zAxisPercentage = axisValueInPercentage;
					continue; // Go to next component.
				}
				// Other axis
				JLabel progressBarLabel = new JLabel(component.getName());
				JProgressBar progressBar = new JProgressBar(0, 100);
				progressBar.setValue(axisValueInPercentage);
				axesPanel.add(progressBarLabel);
				axesPanel.add(progressBar);
			}

		}

		window.setControllerButtons(buttonsPanel);
		// set x and y axes,
		window.setXYAxis(xAxisPercentage, yAxisPercentage);
		// add other axes panel to window.
		window.addAxisPanel(axesPanel);
		int callibration = 1;
		moveRoboClaws(mecanumDriver, xAxisPercentage + callibration,
				yAxisPercentage + callibration, zAxisPercentage + callibration,
				getConnection());
	}

	private void updateCar(MecanumDriver mecanumDriver) {
		String controllerName = window.getSelectedCarDevicesName();
		switch (controllerName) {
		case "keyboard":
			updateCarKeyboard(mecanumDriver);
			break;
		case "joystick":
			updateCarJoystick(mecanumDriver);
		case "leap":
			updateCarLeap(mecanumDriver);
			break;
		/*
		 * case "myo": updateArmMyo(); break;
		 */
		case "phantom":
			// updateCarPhantom();
			break;
		}

		// updateCarLeapMotion()

	}

	private void startShowingControllerData() {

		if (carControllersList.isEmpty())
			window.addCarControllerName("No controller found!");
		if (leapArmController == null && keyboardArmController == null)
			window.addArmControllerName("No controller found!");

		connection = connectToPanda(ControllerSettings.pandaIP,
				ControllerSettings.pandaPort);

		MecanumDriver mecanumDriver = new MecanumDriver();

		initializeArm();
		sleep = 200;
		while (true) {
			sleep = 200;
			updateArm(mecanumDriver);
			updateCar(mecanumDriver);

			// We have to give processor some rest.
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException ex) {
				Logger.getLogger(ControllersHandler.class.getName()).log(
						Level.SEVERE, null, ex);
			}

		}
		// connection.getAmberClient().terminate();
		// for end

	}
	private int[] calculateGripStrength(int[] keys) {
		int[] calculateServosMovement = { 0, 0, 0, 0, 0, 0 };
		int sum12 = keys[0] + keys[1];
		servosPositions.setCatcherPosition(servosPositions.getCatcherPosition()
				+ compareArmKeysWithValue(sum12, keys[0],10));

		return calculateServosMovement;
	}
	private int[] calculateDirectionOfEachServo(int[] keys) {
		int[] calculateServosMovement = { 0, 0, 0, 0, 0, 0 };
		int sumNM = keys[0] + keys[1], sumGV = keys[2] + keys[3], sumUH = keys[4]
				+ keys[5], sum9I = keys[6] + keys[7], sumJK = keys[8] + keys[9], sumOP = keys[10]
				+ keys[11];
		servosPositions.setBottomPosition(servosPositions.getBottomPosition()
				+ compareArmKeys(sumNM, keys[0]));
		servosPositions.setDOF1Position(servosPositions.getDOF1Position()
				+ compareArmKeys(sumGV, keys[2]));
		servosPositions.setDOF2Position(servosPositions.getDOF2Position()
				+ compareArmKeys(sumUH, keys[4]));
		servosPositions.setDOF3Position(servosPositions.getDOF3Position()
				+ compareArmKeys(sum9I, keys[6]));
		servosPositions.setCatcherRotatorPosition(servosPositions
				.getCatcherRotatorPosition() + compareArmKeys(sumJK, keys[8]));
		servosPositions.setCatcherPosition(servosPositions.getCatcherPosition()
				+ compareArmKeys(sumOP, keys[10]));

		return calculateServosMovement;
	}

	private int compareArmKeys(int sum, int key) {
		if (sum == 2 || sum == 0) {
			return 0;
		} else {
			if (key == 1)
				return -2;
			else
				return 2;
		}
	}
	private int compareArmKeysWithValue(int sum, int key, int value) {
		if (sum == 2 || sum == 0) {
			return 0;
		} else {
			if (key == 1)
				return -value;
			else
				return value;
		}
	}

	private int[] calculateKeyBoardAxisPercentage(int[] keys) {
		int[] calculatedKeyBoardAxisPercentage = { 0, 0, 0 };
		int sumAD = keys[0] + keys[1], sumWS = keys[2] + keys[3], sumQE = keys[4]
				+ keys[5];
		calculatedKeyBoardAxisPercentage[0] = compareKeys(sumAD, keys[0]);
		calculatedKeyBoardAxisPercentage[1] = compareKeys(sumWS, keys[2]);
		calculatedKeyBoardAxisPercentage[2] = compareKeys(sumQE, keys[4]);

		return calculatedKeyBoardAxisPercentage;
	}

	private int compareKeys(int sum, int key) {
		if (sum == 2 || sum == 0) {
			return 50;
		} else {
			if (key == 1)
				return 0;
			else
				return 100;
		}
	}

	private void moveRoboClaws(MecanumDriver mecanumDriver,
			int xAxisPercentage, int yAxisPercentage, int zAxisPercentage,
			ConnectionBuilder connection) {
		double divider = 50.0;
		int multiplyer = 4, zeroer = 50;

		if (isDebugEnabled) {
			System.out.println("real left right  value: " + xAxisPercentage);
			System.out.println("real forward backward value: "
					+ yAxisPercentage);
			System.out.println("real axes left right  value: "
					+ zAxisPercentage);
		}

		// it works with joystick
		RoboclawsSpeedValues roboclawsSpeedValues = mecanumDriver
				.joystickDrive((xAxisPercentage - zeroer) / divider,
						(yAxisPercentage - zeroer) / divider,
						(zAxisPercentage - zeroer) / divider);

		try {
			getConnection().getRoboclawProxy()
					.sendMotorsCommand(
							(int) roboclawsSpeedValues.getFrontLeftSpeed()
									* multiplyer,
							(int) roboclawsSpeedValues.getFrontRightSpeed()
									* multiplyer,
							(int) roboclawsSpeedValues.getRearLeftSpeed()
									* multiplyer,
							(int) roboclawsSpeedValues.getRearRightSpeed()
									* multiplyer);
		} catch (IOException e) {
			// Swallow
		}

	}

}
