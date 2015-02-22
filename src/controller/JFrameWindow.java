package controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.controllers.ControllersHandler;
import net.java.games.input.Component;

/**
 * 
 * Joystick Test Window
 * 
 * 
 * @author TheUzo007 http://theuzo007.wordpress.com
 * 
 *         Created 22 Oct 2013
 * 
 */
public class JFrameWindow extends javax.swing.JFrame {

	/**
	 * Creates new form JFrameWindow
	 */
	public JFrameWindow(ControllersHandler controllersHandler) {
		// Set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		initComponents(controllersHandler);

		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents(ControllersHandler controllersHandler) {

		jPanelAxes = new javax.swing.JPanel();
		jLabelXYAxis = new javax.swing.JLabel();
		jPanelXYAxis = new javax.swing.JPanel();
		jPanel_forAxis = new javax.swing.JPanel();
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons_arm = new javax.swing.JPanel();
		jPanelHatSwitch = new javax.swing.JPanel();
		jComboBox_carControllers = new javax.swing.JComboBox();
		jComboBox_armControllers = new javax.swing.JComboBox();
		jComboBox_armControllers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controllersHandler.setListeners();
			}
		});

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("JInput Joystick Test");

		jPanelAxes.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Axes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
				new java.awt.Color(0, 51, 204)));

		jLabelXYAxis.setText("X Axis / Y Axis");

		jPanelXYAxis.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanelXYAxis.setPreferredSize(new java.awt.Dimension(111, 111));

		javax.swing.GroupLayout jPanelXYAxisLayout = new javax.swing.GroupLayout(
				jPanelXYAxis);
		jPanelXYAxis.setLayout(jPanelXYAxisLayout);
		jPanelXYAxisLayout.setHorizontalGroup(jPanelXYAxisLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 109, Short.MAX_VALUE));
		jPanelXYAxisLayout.setVerticalGroup(jPanelXYAxisLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 109, Short.MAX_VALUE));

		javax.swing.GroupLayout jPanel_forAxisLayout = new javax.swing.GroupLayout(
				jPanel_forAxis);
		jPanel_forAxis.setLayout(jPanel_forAxisLayout);
		jPanel_forAxisLayout.setHorizontalGroup(jPanel_forAxisLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE));
		jPanel_forAxisLayout.setVerticalGroup(jPanel_forAxisLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		javax.swing.GroupLayout jPanelAxesLayout = new javax.swing.GroupLayout(
				jPanelAxes);
		jPanelAxes.setLayout(jPanelAxesLayout);
		jPanelAxesLayout
				.setHorizontalGroup(jPanelAxesLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelAxesLayout
										.createSequentialGroup()
										.addGroup(
												jPanelAxesLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanelAxesLayout
																		.createSequentialGroup()
																		.addGap(58,
																				58,
																				58)
																		.addComponent(
																				jLabelXYAxis))
														.addGroup(
																jPanelAxesLayout
																		.createSequentialGroup()
																		.addGap(37,
																				37,
																				37)
																		.addComponent(
																				jPanelXYAxis,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(
												jPanel_forAxis,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));
		jPanelAxesLayout
				.setVerticalGroup(jPanelAxesLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelAxesLayout
										.createSequentialGroup()
										.addComponent(jLabelXYAxis)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jPanelXYAxis,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 16, Short.MAX_VALUE))
						.addComponent(jPanel_forAxis,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE));

		jPanelButtons.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Buttons",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
				new java.awt.Color(0, 51, 204)));

		javax.swing.GroupLayout jPanelButtonsLayout = new javax.swing.GroupLayout(
				jPanelButtons);
		jPanelButtons.setLayout(jPanelButtonsLayout);

		jPanelButtonsLayout.setHorizontalGroup(jPanelButtonsLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 248, Short.MAX_VALUE));
		jPanelButtonsLayout.setVerticalGroup(jPanelButtonsLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 215, Short.MAX_VALUE));

		jPanelButtons_arm.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Buttons",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
						new java.awt.Color(0, 51, 204)));

		javax.swing.GroupLayout jPanelButtonsLayout_arm = new javax.swing.GroupLayout(
				jPanelButtons_arm);
		jPanelButtons_arm.setLayout(jPanelButtonsLayout_arm);

		jPanelButtonsLayout_arm.setHorizontalGroup(jPanelButtonsLayout_arm
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 248, Short.MAX_VALUE));
		jPanelButtonsLayout_arm.setVerticalGroup(jPanelButtonsLayout_arm
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 215, Short.MAX_VALUE));

		jPanelHatSwitch.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Hat Switch",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
				new java.awt.Color(0, 51, 204)));

		javax.swing.GroupLayout jPanelHatSwitchLayout = new javax.swing.GroupLayout(
				jPanelHatSwitch);
		jPanelHatSwitch.setLayout(jPanelHatSwitchLayout);
		jPanelHatSwitchLayout.setHorizontalGroup(jPanelHatSwitchLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 121, Short.MAX_VALUE));
		jPanelHatSwitchLayout.setVerticalGroup(jPanelHatSwitchLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		jComboBox_carControllers
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jComboBox_controllersActionPerformed(evt);
					}
				});

		jComboBox_armControllers
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jComboBox_armControllersActionPerformed(evt);
					}
				});

		Container left_container = new Container();
		Container right_container = new Container();
		Container middle_container = new Container();
		// left_container.setLayout(layout);
		javax.swing.GroupLayout layout_left = new javax.swing.GroupLayout(
				left_container);
		left_container.setLayout(layout_left);
		layout_left
				.setHorizontalGroup(layout_left
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout_left
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout_left
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																layout_left
																		.createSequentialGroup()
																		.addGroup(
																				layout_left
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addGroup(
																								layout_left
																										.createSequentialGroup()
																										.addComponent(
																												jPanelButtons,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addComponent(
																												jPanelHatSwitch,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE))
																						.addComponent(
																								jPanelAxes,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addContainerGap())
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																layout_left
																		.createSequentialGroup()
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)
																		.addComponent(
																				jComboBox_carControllers,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				237,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(88,
																				88,
																				88)))));
		layout_left
				.setVerticalGroup(layout_left
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout_left
										.createSequentialGroup()
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(
												jComboBox_carControllers,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												jPanelAxes,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout_left
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																jPanelButtons,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jPanelHatSwitch,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		javax.swing.GroupLayout layout_right = new javax.swing.GroupLayout(
				right_container);
		right_container.setLayout(layout_right);
		layout_right
				.setHorizontalGroup(layout_right
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout_right
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout_right
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																layout_right
																		.createSequentialGroup()
																		.addGroup(
																				layout_right
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addGroup(
																								layout_right
																										.createSequentialGroup()
																										.addComponent(
																												jPanelButtons_arm,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
																		.addContainerGap())
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																layout_right
																		.createSequentialGroup()
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)
																		.addComponent(
																				jComboBox_armControllers,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				237,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(88,
																				88,
																				88)))));
		layout_right
				.setVerticalGroup(layout_right
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout_right
										.createSequentialGroup()
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(
												jComboBox_armControllers,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												layout_right
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																jPanelButtons_arm,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));

		getContentPane().setLayout(new GridLayout(1, 3));
		getContentPane().add(left_container);
		getContentPane().add(middle_container);
		getContentPane().add(right_container);
		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jComboBox_controllersActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox_controllersActionPerformed
		// When another controller is selected we have to remove old stuff.
		jPanelButtons.removeAll();
		jPanelButtons.repaint();
		jPanel_forAxis.removeAll();
		jPanel_forAxis.repaint();
	}// GEN-LAST:event_jComboBox_controllersActionPerformed

	private void jComboBox_armControllersActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox_controllersActionPerformed
		// When another controller is selected we have to remove old stuff.
		jPanelButtons_arm.removeAll();
		jPanelButtons_arm.repaint();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JComboBox jComboBox_carControllers;
	private javax.swing.JComboBox jComboBox_armControllers;
	private javax.swing.JLabel jLabelXYAxis;
	private javax.swing.JPanel jPanelAxes;
	private javax.swing.JPanel jPanelButtons;
	private javax.swing.JPanel jPanelButtons_arm;
	private javax.swing.JPanel jPanelHatSwitch;
	private javax.swing.JPanel jPanelXYAxis;
	private javax.swing.JPanel jPanel_forAxis;

	// End of variables declaration//GEN-END:variables

	/* Methods for setting components on the window. */

	public int getSelectedCarControllerName() {
		return jComboBox_carControllers.getSelectedIndex();
	}

	public String getSelectedArmDevicesName() {
		return (String) jComboBox_armControllers.getSelectedItem();
	}

	public void addCarControllerName(String controllerName) {
		jComboBox_carControllers.addItem(controllerName);
	}

	public void addArmControllerName(String controllerName) {

		jComboBox_armControllers.addItem(controllerName);
	}

	public void removeArmControllerName(String controllerName) {

		jComboBox_armControllers.removeItem(controllerName);
	}

	public void showControllerDisconnected() {
		jComboBox_carControllers.removeAllItems();
		jComboBox_carControllers.addItem("Controller disconnected!");
	}

	public void showArmDevicesNameDisconnected() {
		jComboBox_armControllers.removeAllItems();
		jComboBox_armControllers.addItem("All arm devices disconnected!");
	}

	public void setXYAxis(int xPercentage, int yPercentage) {
		Graphics2D g2d = (Graphics2D) jPanelXYAxis.getGraphics();
		g2d.clearRect(1, 1, jPanelXYAxis.getWidth() - 2,
				jPanelXYAxis.getHeight() - 2);
		g2d.fillOval(xPercentage, yPercentage, 10, 10);
	}

	public void setControllerButtons(JPanel buttonsPanel) {
		jPanelButtons.removeAll();
		jPanelButtons.add(buttonsPanel);
		jPanelButtons.validate();
	}

	public void setArmDevicesButtons(JPanel buttonsPanel) {
		jPanelButtons_arm.removeAll();
		jPanelButtons_arm.add(buttonsPanel);
		jPanelButtons_arm.validate();
	}

	private JCheckBox hardcoredCheckBox;
	private JCheckBox armForwardBackwardCheckbox;
	private JCheckBox armUpDownCheckbox;
	private JCheckBox armLeftRightCheckBox;
	private JCheckBox handPitchCheckBox;
	private JCheckBox handRollCheckBox;
	private JCheckBox distanceOfFingersCheckBox;
	private JLabel armStateInfo;

	private void initializeCheckBoxes() {
		int xCheckBoxPositionInPanel = 15;
		int checkBoxWidth = 200;
		int checkBoxHeight = 20;
		int yCheckBoxPositionInPanel = 15;
		hardcoredCheckBox = new JCheckBox("Hardcored leap boundaries", true);
		hardcoredCheckBox.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		yCheckBoxPositionInPanel += checkBoxHeight;
		armForwardBackwardCheckbox = new JCheckBox(
				"Arm forward/backward boundaries", false);
		armForwardBackwardCheckbox.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		armForwardBackwardCheckbox.setEnabled(false);
		yCheckBoxPositionInPanel += checkBoxHeight;
		armUpDownCheckbox = new JCheckBox("Arm up/down boundaries", false);
		armUpDownCheckbox.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		armUpDownCheckbox.setEnabled(false);
		yCheckBoxPositionInPanel += checkBoxHeight;
		armLeftRightCheckBox = new JCheckBox("Arm left/right boundaries", false);
		armLeftRightCheckBox.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		armLeftRightCheckBox.setEnabled(false);
		yCheckBoxPositionInPanel += checkBoxHeight;
		handRollCheckBox = new JCheckBox("Arm roll boundaries", false);
		handRollCheckBox.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		handRollCheckBox.setEnabled(false);
		yCheckBoxPositionInPanel += checkBoxHeight;
		handPitchCheckBox = new JCheckBox("Arm pitch boundaries", false);
		handPitchCheckBox.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		handPitchCheckBox.setEnabled(false);
		
		yCheckBoxPositionInPanel += checkBoxHeight;
		distanceOfFingersCheckBox = new JCheckBox(
				"Fingers distance boundaries", false);
		distanceOfFingersCheckBox.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		distanceOfFingersCheckBox.setEnabled(false);
		yCheckBoxPositionInPanel += checkBoxHeight;
		armStateInfo = new JLabel();
		armStateInfo.setBounds(xCheckBoxPositionInPanel,
				yCheckBoxPositionInPanel, checkBoxWidth, checkBoxHeight);
		armStateInfo.setVisible(true);

	}

	public void setArmLeapCheckBox() {
		initializeCheckBoxes();
		hardcoredCheckBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (hardcoredCheckBox.isSelected()) {
					boolean checboEnabled = false;
					armForwardBackwardCheckbox.setEnabled(checboEnabled);
					armUpDownCheckbox.setEnabled(checboEnabled);
					armLeftRightCheckBox.setEnabled(checboEnabled);
					handPitchCheckBox.setEnabled(checboEnabled);
					
					handRollCheckBox.setEnabled(checboEnabled);
					distanceOfFingersCheckBox.setEnabled(checboEnabled);
					armStateInfo.setText("Arm is running with prehardcored boundaries");
				}
				if (!hardcoredCheckBox.isSelected()) {
					boolean checboEnabled = true;
					armForwardBackwardCheckbox.setEnabled(checboEnabled);
					armUpDownCheckbox.setEnabled(checboEnabled);
					armLeftRightCheckBox.setEnabled(checboEnabled);
					handPitchCheckBox.setEnabled(checboEnabled);
					handPitchCheckBox.setSelected(true);
					handRollCheckBox.setEnabled(checboEnabled);
					distanceOfFingersCheckBox.setEnabled(checboEnabled);
					armStateInfo.setText("Arm is moving with customized boundaries");
				}

			}
		});
		jPanelButtons_arm.removeAll();
		jPanelButtons_arm.add(hardcoredCheckBox);
		jPanelButtons_arm.add(armForwardBackwardCheckbox);
		jPanelButtons_arm.add(armUpDownCheckbox);
		jPanelButtons_arm.add(armLeftRightCheckBox);
		jPanelButtons_arm.add(handPitchCheckBox);
		jPanelButtons_arm.add(handRollCheckBox);
		jPanelButtons_arm.add(distanceOfFingersCheckBox);
		jPanelButtons_arm.add(armStateInfo);
		jPanelButtons_arm.validate();
	}
	public void setArmStateInfo(String text){
		armStateInfo.setText(text);
	}

	public boolean isAtLeastOneBoundaryCheckBoxSelected() {
		return isArmForwardBackwardCheckboxSelected()
				|| isArmLeftRightCheckboxSelected()
				|| isArmUpDownCheckboxSelected()
				|| isHandPitchCheckboxSelected()
				|| isHandRollCheckboxSelected()
				|| isDistanceOfFingersCheckboxSelected();
	}

	public boolean areLeapBoundariesHardcoded() {
		return hardcoredCheckBox.isSelected();
	}

	public boolean isArmForwardBackwardCheckboxSelected() {
		return armForwardBackwardCheckbox.isSelected();
	}

	public boolean isArmUpDownCheckboxSelected() {
		return armUpDownCheckbox.isSelected();
	}

	public boolean isArmLeftRightCheckboxSelected() {
		return armLeftRightCheckBox.isSelected();
	}

	public boolean isHandPitchCheckboxSelected() {
		return handPitchCheckBox.isSelected();
	}

	public boolean isHandRollCheckboxSelected() {
		return handRollCheckBox.isSelected();
	}

	public boolean isDistanceOfFingersCheckboxSelected() {
		return distanceOfFingersCheckBox.isSelected();
	}

	public void setHatSwitch(float hatSwitchPosition) {
		int circleSize = 100;

		Graphics2D g2d = (Graphics2D) jPanelHatSwitch.getGraphics();
		g2d.clearRect(5, 15, jPanelHatSwitch.getWidth() - 10,
				jPanelHatSwitch.getHeight() - 22);
		g2d.drawOval(20, 22, circleSize, circleSize);

		if (Float.compare(hatSwitchPosition, Component.POV.OFF) == 0)
			return;

		int smallCircleSize = 10;
		int upCircleX = 65;
		int upCircleY = 17;
		int leftCircleX = 15;
		int leftCircleY = 68;
		int betweenX = 37;
		int betweenY = 17;

		int x = 0;
		int y = 0;

		g2d.setColor(Color.blue);

		if (Float.compare(hatSwitchPosition, Component.POV.UP) == 0) {
			x = upCircleX;
			y = upCircleY;
		} else if (Float.compare(hatSwitchPosition, Component.POV.DOWN) == 0) {
			x = upCircleX;
			y = upCircleY + circleSize;
		} else if (Float.compare(hatSwitchPosition, Component.POV.LEFT) == 0) {
			x = leftCircleX;
			y = leftCircleY;
		} else if (Float.compare(hatSwitchPosition, Component.POV.RIGHT) == 0) {
			x = leftCircleX + circleSize;
			y = leftCircleY;
		} else if (Float.compare(hatSwitchPosition, Component.POV.UP_LEFT) == 0) {
			x = upCircleX - betweenX;
			y = upCircleY + betweenY;
		} else if (Float.compare(hatSwitchPosition, Component.POV.UP_RIGHT) == 0) {
			x = upCircleX + betweenX;
			y = upCircleY + betweenY;
		} else if (Float.compare(hatSwitchPosition, Component.POV.DOWN_LEFT) == 0) {
			x = upCircleX - betweenX;
			y = upCircleY + circleSize - betweenY;
		} else if (Float.compare(hatSwitchPosition, Component.POV.DOWN_RIGHT) == 0) {
			x = upCircleX + betweenX;
			y = upCircleY + circleSize - betweenY;
		}

		g2d.fillOval(x, y, smallCircleSize, smallCircleSize);
	}

	public void addAxisPanel(javax.swing.JPanel axesPanel) {
		jPanel_forAxis.removeAll();
		jPanel_forAxis.add(axesPanel);
		jPanel_forAxis.validate();
	}
}
