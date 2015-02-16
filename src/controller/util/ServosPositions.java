package controller.util;

import Maestro.ServoBoundaries;
import Maestro.ServosInitialValues;

public class ServosPositions {

	private int bottom, dof1, dof2, dof3, catcherRotator, catcher;

	public ServosPositions(int armLeftRightP, int armUpDownP,
			int armForwardBackwardP, int handPitchP, int handRollP,
			int distanceOfFingersP) {
		bottom = armLeftRightP;
		dof1 = armUpDownP;
		dof2 = armForwardBackwardP;
		dof3 = handPitchP;
		catcherRotator = handRollP;
		catcher = distanceOfFingersP;
	}

	public void setBottomPosition(int bottomP) {
		if (bottomP >= ServoBoundaries.BOTTOM.getServoMinValue()
				&& bottomP <= ServoBoundaries.BOTTOM.getServoMaxValue()) {
			bottom = bottomP;
		}
	}

	public int getBottomPosition() {
		return bottom;
	}

	public int getDOF1Position() {
		return dof1;
	}

	public void setDOF1Position(int dof1P) {
		if (dof1P >= ServoBoundaries.DOF_1.getServoMinValue()
				&& dof1P <= ServoBoundaries.DOF_1.getServoMaxValue()) {
			dof1 = dof1P;
		}
	}

	public int getDOF2Position() {
		return dof2;
	}

	public void setDOF2Position(int dof2P) {
		if (dof2P >= ServoBoundaries.DOF_2.getServoMinValue()
				&& dof2P <= ServoBoundaries.DOF_2.getServoMaxValue()) {
			dof2 = dof2P;
		}
	}

	public int getDOF3Position() {
		return dof3;
	}

	public void setDOF3Position(int dof3P) {
		if (dof3P >= ServoBoundaries.DOF_3.getServoMinValue()
				&& dof3P <= ServoBoundaries.DOF_3.getServoMaxValue()) {
			dof3 = dof3P;
		}
	}

	public int getCatcherRotatorPosition() {
		return catcherRotator;
	}

	public void setCatcherRotatorPosition(int catcherRotatorP) {
		if (catcherRotatorP >= ServoBoundaries.CATCHER_ROTATOR
				.getServoMinValue()
				&& catcherRotatorP <= ServoBoundaries.CATCHER_ROTATOR
						.getServoMaxValue()) {
			catcherRotator = catcherRotatorP;
		}
	}

	public int getCatcherPosition() {
		return catcher;
	}

	public void setCatcherPosition(int catcherP) {
		if (catcherP >= ServoBoundaries.CATCHER.getServoMinValue()
				&& catcherP <= ServoBoundaries.CATCHER.getServoMaxValue()) {
			catcher = catcherP;
		}
	}

}
