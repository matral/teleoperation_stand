package controller.controllers;

import controller.util.RoboclawsSpeedValues;

public class MecanumDriver {

	// int xMid = 2048, yMid = 2048, zMid = 2048;
	public double limit(double val) {
		if (val > 1.0)
			return 1.0;
		if (val < -1.0)
			return -1.0;
		return val;
	}

	void normalize(double[] vals) {
		double max = 0;

		for (double val : vals)
			if (Math.abs(val) > max)
				max = Math.abs(val);

		if (max > 1.0)
			for (int i = 0; i < vals.length; i++)
				vals[i] /= max;
	}

	public RoboclawsSpeedValues mecanumDrive(double magnitude,
			double direction, double rotation) {

		RoboclawsSpeedValues roboclawsSpeedValues = new RoboclawsSpeedValues(0,
				0, 0, 0);

		int frontRightDir = 1;
		int frontLeftDir = 1;
		int rearRightDir = 1;
		int rearLeftDir = 1;

		double m_maxOutput = 100.0;

		// Limit limits magnitude to 1.0
		magnitude = limit(magnitude);
		boolean isDebugEnabled = false;
		if (isDebugEnabled) {
			System.out.println("magnitude  " + magnitude);
			System.out.println("direction " + direction);
			System.out.println("rotation  " + rotation);
		}
		// Normalized for full power along the Cartesian axes.
		magnitude = magnitude * Math.sqrt(2.0);

		// magnitude has value from 0 to 1.41, direction has value from 0 to 360
		// and rotation has value from -1 to 1
		double rollers = 45.0;
		// The rollers are at 45 degree angles.
		double dirInRad = (direction + rollers) * Math.PI / 180.0;
		double cosD = Math.cos(dirInRad);
		double sinD = Math.sin(dirInRad);
		double[] wheelSpeeds = new double[4];

		dirInRad = -dirInRad;

		if (isDebugEnabled) {
			System.out.println("dir in rad " + dirInRad);
			System.out.println("cosD " + cosD);
			System.out.println("sinD " + sinD);
		}
		isDebugEnabled = false;

		wheelSpeeds[0] = sinD * magnitude + rotation;
		wheelSpeeds[1] = cosD * magnitude - rotation;
		wheelSpeeds[2] = cosD * magnitude + rotation;
		wheelSpeeds[3] = sinD * magnitude - rotation;

		// Only if any are > 1.0
		normalize(wheelSpeeds);

		roboclawsSpeedValues.setRoboclawsSpeedValues(wheelSpeeds[0]
				* m_maxOutput * frontLeftDir, wheelSpeeds[1] * m_maxOutput
				* frontRightDir, wheelSpeeds[2] * m_maxOutput * rearLeftDir,
				wheelSpeeds[3] * m_maxOutput * rearRightDir);

		return roboclawsSpeedValues;
	}

	public RoboclawsSpeedValues joystickDrive(double x, double y, double z) {

		double heading = 0, headingLockPoint = 0;

		boolean isDebugEnabled = true;
		if (isDebugEnabled) {
			System.out.println("left right:  " + x);
			System.out.println("forward backward " + y);
			System.out.println("axis left right: " + z);
		}
		isDebugEnabled = false;
		// Dead Space
		double xyDeadSpace = 0;
		double zDeadSpace = 0;

		// Check that the position is outside the deadspace
		double newx = (Math.abs(x) - xyDeadSpace) * (xyDeadSpace + 1);
		if (newx < 0)
			newx = 0;
		x = (x < 0) ? -newx : newx;
		double newy = (Math.abs(y) - xyDeadSpace) * (xyDeadSpace + 1);
		if (newy < 0)
			newy = 0;
		y = (y < 0) ? -newy : newy;
		double newz = (Math.abs(z) - zDeadSpace) * (zDeadSpace + 1);
		if (newz < 0)
			newz = 0;
		z = (z < 0) ? -newz : newz;

		x = limit(x); // these values should saturate at 1
		y = limit(y);
		z = limit(z);

		double magnitude = Math.sqrt(x * x + y * y);
		double direction = Math.atan2(x, y);
		double rotation = z;

		if (magnitude == 0)
			direction = 0;

		// Into degrees
		direction = direction * 180.0 / Math.PI;

		boolean gyroLocked = false;
		if (gyroLocked) {
			direction -= heading;
			direction += headingLockPoint;
		}

		if (direction < 0)
			direction += 360;
		if (direction > 360)
			direction -= 360;

		return mecanumDrive(magnitude, direction, rotation);
	}

}
