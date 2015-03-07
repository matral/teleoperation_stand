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
import Leap.LeapListener;
import Maestro.PololuConnector;
import Maestro.Servo;
import Maestro.ServosInitialValues;

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
		//int keyboardSpeed = 13;
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
		//int keyboardSpeed = 13;
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

	private void activateJoystickForCar() {
		if (leapCarController != null) {
			if (leapCarListener != null) {
				leapCarController.removeListener(leapCarListener);
			}
		}
		sleep = 5;
		//int keyboardSpeed = 13;
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
		//int keyboardSpeed = 13;
		// setSpeed(keyboardSpeed);
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

	private void registerPhantomForArm() {
		window.addArmControllerName("phantom");
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

	private void activatePhantomForArm() {
		if (leapArmController != null) {
			if (leapArmListener != null) {
				leapArmController.removeListener(leapArmListener);
			}
		}
	}

	private void searchForArmControllers() {
		// System.out.println("1");
		registerKeyboardForArm();

		registerLeapForArm();
		registerJoystickForArm();
		// registerPhantomForArm();
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
		case "phantom":
			activatePhantomForArm();
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
		
		case "phantom":
			// updateArmLeap();
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
						System.out.println("pitch: " + direction.pitch());
						System.out.println("yaw: " + direction.yaw());
						System.out.println("roll: " + normal.roll());

						// leapCarListener.setCarPosition()
						/*
						 * moveRoboClaws(mecanumDriver, xAxisPercentage,
						 * yAxisPercentage, zAxisPercentage, getConnection());
						 */

					} else {
						int pitchPercentage;
						int yawPercentage;
						int rollPercentage;
						int[] calculatedLeapHandPercentage;
						calculatedLeapHandPercentage = leapCarListener
								.leapHandPositionsRescaled(direction.pitch(),
										normal.roll(), direction.yaw(),
										connection);
						pitchPercentage = calculatedLeapHandPercentage[0];
						rollPercentage = calculatedLeapHandPercentage[1];
						yawPercentage = calculatedLeapHandPercentage[2];
						
						
						System.out.println("pitch: " + pitchPercentage);
						System.out.println("yaw: " + yawPercentage);
						System.out.println("roll: " + rollPercentage);
						//moveRoboClaws(mecanumDriver, pitchPercentage,
							//	yawPercentage, rollPercentage, connection);
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

			if (keys_arm[0] + keys_arm[1] != 0) {
				System.out.println(servosPositions.getBottomPosition());
				connection.getHitecProxy().setAngle(
						Servo.BOTTOM.getServoPort(),
						servosPositions.getBottomPosition());

				/*
				 * PololuConnector.setTarget(servosPositions.getBottomPosition(),
				 * Servo.BOTTOM.getServoPort());
				 */}
			if (keys_arm[2] + keys_arm[3] != 0) {

				getConnection().getHitecProxy().setAngle(
						Servo.DOF_1.getServoPort(),
						servosPositions.getDOF1Position());
				/*
				 * PololuConnector.setTarget(servosPositions.getDOF1Position(),
				 * Servo.DOF_1.getServoPort());
				 */
			}
			if (keys_arm[4] + keys_arm[5] != 0) {
				getConnection().getHitecProxy().setAngle(
						Servo.DOF_2.getServoPort(),
						servosPositions.getDOF2Position());
				/*
				 * PololuConnector.setTarget(servosPositions.getDOF2Position(),
				 * Servo.DOF_2.getServoPort());
				 */
			}

			if (keys_arm[6] + keys_arm[7] != 0) {
				getConnection().getHitecProxy().setAngle(
						Servo.DOF_3.getServoPort(),
						servosPositions.getDOF3Position());
				/*
				 * PololuConnector.setTarget(servosPositions.getDOF3Position(),
				 * Servo.DOF_3.getServoPort());
				 */
			}

			if (keys_arm[8] + keys_arm[9] != 0) {
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
				getConnection().getHitecProxy().setAngle(
						Servo.CATCHER.getServoPort(),
						servosPositions.getCatcherPosition());
				/*
				 * PololuConnector.setTarget(servosPositions.getCatcherPosition()
				 * , Servo.CATCHER.getServoPort());
				 */
			}

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

	private void updateArmJoystick() {

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

		window.setArmDevicesButtons(buttonsPanel);
		// set x and y axes,
		window.setArmXYAxis(xAxisPercentage, yAxisPercentage);
		// add other axes panel to window.
		window.addAxisPanel(axesPanel);

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
		moveRoboClaws(mecanumDriver, xAxisPercentage, yAxisPercentage,
				zAxisPercentage, getConnection());
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
		int multiplyer = 4, zeroer = 49;

		// it works with joystick
		RoboclawsSpeedValues roboclawsSpeedValues = mecanumDriver
				.joystickDrive((xAxisPercentage - zeroer) / divider,
						(yAxisPercentage - zeroer - 2) / divider,
						(zAxisPercentage - zeroer) / divider);

		// double[] keys = setKeys();

		// JoystickDrive(keys[0],keys[1],keys[2]);

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
