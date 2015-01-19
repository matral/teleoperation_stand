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
import net.java.games.input.Keyboard;
import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;
import controller.JFrameWindow;
import controller.util.ConnectionBuilder;
import controller.util.RoboclawsSpeedValues;
import controller.util.Util;

public class ControllersHandler {

	public static void main(String args[]) {
		new ControllersHandler();
	}

	final JFrameWindow window;
	private ArrayList<Controller> controllersList;

	public ControllersHandler() {
		window = new JFrameWindow();

		controllersList = new ArrayList<>();
		searchForControllers();

		// If at least one controller was found we start showing controller data
		// on window.
		if (!controllersList.isEmpty())
			startShowingControllerData();
		else
			window.addControllerName("No controller found!");
	}

	private void searchForControllers() {

		Controller[] controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];

			if (controller.getType() == Controller.Type.STICK
					|| controller.getType() == Controller.Type.GAMEPAD
					|| controller.getType() == Controller.Type.WHEEL
					|| controller.getType() == Controller.Type.FINGERSTICK
					|| controller.getType() == Controller.Type.KEYBOARD
					|| controller.getType() == Controller.Type.MOUSE) {
				// Add new controller to the list of all controllers.
				controllersList.add(controller);

				// Add new controller to the list on the window.
				window.addControllerName(controller.getName() + " - "
						+ controller.getType().toString() + " type");
			}
			if (controller.getType() == Controller.Type.KEYBOARD){
				window.addArmDevicesName(controller.getName() + " - "
						+ controller.getType().toString() + " type");
			}
		}
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

		return new ConnectionBuilder(client, new RoboclawProxy(client, 0));

	}

	private void startShowingControllerData() {

		String pandaIP = "192.168.2.203";
		int pandaPort = 26233;
		ConnectionBuilder connection = connectToPanda(pandaIP, pandaPort);

		MecanumDriver mecanumDriver = new MecanumDriver();

		while (true) {
			int[] keys = { 0,0,0,0,0,0 };
			// Currently selected controller.

			Controller controller = controllersList.get(window
					.getSelectedControllerName());

			// Pull controller for current data, and break while loop if
			// controller is disconnected.
			if (!controller.poll()) {
				window.showControllerDisconnected();
				break;
			}

			// X axis and Y axis
			int xAxisPercentage = 0, yAxisPercentage = 0, zAxisPercentage = 0;
			// JPanel for other axes.
			JPanel axesPanel = new JPanel(
					new FlowLayout(FlowLayout.LEFT, 25, 2));
			axesPanel.setBounds(0, 0, 200, 190);

			// JPanel for controller buttons
			JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1,
					1));
			buttonsPanel.setBounds(6, 19, 250, 210);
			
			JPanel buttonsPanel_arm = new JPanel(new FlowLayout(FlowLayout.LEFT, 1,
					1));
			buttonsPanel_arm.setBounds(6, 19, 250, 210);

			// Go trough all components of the controller.
			Component[] components = controller.getComponents();

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
					JToggleButton aToggleButton = new JToggleButton(
							buttonIndex, isItPressed);
					aToggleButton.setPreferredSize(new Dimension(48, 25));
					aToggleButton.setEnabled(false);
					buttonsPanel.add(aToggleButton);
					
					JToggleButton aToggleButton_arm = new JToggleButton(
							buttonIndex, isItPressed);
					aToggleButton_arm.setPreferredSize(new Dimension(48, 25));
					aToggleButton_arm.setEnabled(false);
					buttonsPanel_arm.add(aToggleButton_arm);
					
					
					
					if(isItPressed){	
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
			int[] calculatedKeyBoardAxisPercentage;
			if(controller.getType() == Controller.Type.KEYBOARD){
				calculatedKeyBoardAxisPercentage = calculateKeyBoardAxisPercentage(keys);
				xAxisPercentage = calculatedKeyBoardAxisPercentage[0];
				yAxisPercentage = calculatedKeyBoardAxisPercentage[1];
				zAxisPercentage = calculatedKeyBoardAxisPercentage[2];
				System.out.println("xaxis:    " + xAxisPercentage);
				System.out.println("yaxis:    " + yAxisPercentage);
				System.out.println("zaxis:    " + zAxisPercentage);
			}

			// Now that we go trough all controller components,
			// we add butons panel to window,
			window.setControllerButtons(buttonsPanel);
			// we add arm butons panel to window,
			window.setArmDevicesButtons(buttonsPanel_arm);
			// set x and y axes,
			window.setXYAxis(xAxisPercentage, yAxisPercentage);
			// add other axes panel to window.
			window.addAxisPanel(axesPanel);

			moveRoboClaws(mecanumDriver, xAxisPercentage, yAxisPercentage,
					zAxisPercentage, connection);

			// We have to give processor some rest.
			try {
				Thread.sleep(25);
			} catch (InterruptedException ex) {
				Logger.getLogger(ControllersHandler.class.getName()).log(
						Level.SEVERE, null, ex);
			}

		}
		connection.getAmberClient().terminate();
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

	private int compareKeys(int sum, int key){
		if (sum == 2 || sum == 0 ) {
			return 50;
		}
		else{
			if(key == 1)
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
			connection.getRoboclawProxy()
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
